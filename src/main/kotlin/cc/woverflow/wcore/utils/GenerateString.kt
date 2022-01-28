package cc.woverflow.wcore.utils

import org.apache.commons.lang3.RandomStringUtils

const val STRING_LENGTH = 50;
const val ALPHANUMERIC_REGEX = "[a-zA-Z0-9]+";
 
fun createAlphanumericString() {
  val randomString = RandomStringUtils.randomAlphanumeric(STRING_LENGTH);
  assert(randomString.matches(Regex(ALPHANUMERIC_REGEX)));
  return randomString;
}
