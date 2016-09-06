package io.findify.cfg4s.error

/**
  * Created by shutty on 9/6/16.
  */
case class KeyNotFoundException(path:List[String]) extends ConfigException
