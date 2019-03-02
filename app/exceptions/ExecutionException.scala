package exceptions

case class ExecutionException(message: String) extends Exception(message) with BusinessLogicException
