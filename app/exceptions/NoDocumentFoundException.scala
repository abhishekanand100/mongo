package exceptions

case class NoDocumentFoundException(message: String) extends Exception(message) with BusinessLogicException
