package my.example
package dataprocessing

import dataprocessing.Cons.cons

sealed trait Processor[-I, +O] { self =>
  def apply(input: LazyStream[I]): LazyStream[O] =
    self match {
      case Consumer(receive) =>
        input match {
          case Cons(h, t) => receive(Some(h()))(t())
          case Nil => receive(None).apply(Nil)
        }
      case Emitter(next, rest) => cons(next, rest.apply(input))
      case Halt => Nil
    }

  def repeat: Processor[I, O] = {
    def replaceHaltWithSelf(processor: Processor[I, O]): Processor[I, O] = {
      processor match {
        case Consumer(receive) => Consumer {
          case None => receive(None) // This is "stop branch", without this it never stops even after consuming everything
          case someValue => replaceHaltWithSelf(receive(someValue))
        }
        case Emitter(next, rest) => Emitter(next, replaceHaltWithSelf(rest))
        case Halt => replaceHaltWithSelf(self)
      }
    }
    replaceHaltWithSelf(self)
  }
}

case class Consumer[I, O](receive: Option[I] => Processor[I, O]) extends Processor[I, O]

case class Emitter[I, O](next: O, rest: Processor[I, O]) extends Processor[I, O]

case object Halt extends Processor[Any, Nothing]

object Processor {
  def consume[I, O](next: I => Processor[I, O]): Processor[I, O] =
    new Consumer[I, O]({
      case Some(input) => next(input)
      case None => Halt
    })

  def transmitOne[I]: Processor[I, I] =
    consume(input => Emitter(input, Halt))

  def transmitter[I]: Processor[I, I] =
    transmitOne.repeat

  def take[I](n: Int): Processor[I, I] =
    if (n <= 0) Halt
    else consume(input => Emitter(input, take(n - 1)))

  def drop[I](n: Int): Processor[I, I] =
    if (n <= 0) transmitter
    else consume(input => drop(n - 1))

}