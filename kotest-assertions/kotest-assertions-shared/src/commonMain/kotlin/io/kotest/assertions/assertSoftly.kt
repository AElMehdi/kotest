package io.kotest.assertions

/**
 * Run multiple assertions and throw a single error after all are executed if any fail
 *
 * This method will run all the assertions inside [assertions] block, and will collect all failures that may happen.
 * It then compact all of them in a single throwable and throw it instead, or nothing if no assertion fail.
 *
 * ```
 *     // All assertions below are going to be executed, even when one or multiple fail.
 *     // All the failures are then collected and thrown in one single throwable.
 *     assertSoftly {
 *         "foo" shouldBe "bar"
 *         "foo" shouldBe "foo
 *         "foo" shouldBe "baz"
 *     }
 * ```
 */
inline fun <T> assertSoftly(assertions: () -> T): T {
  // Handle the edge case of nested calls to this function by only calling throwCollectedErrors in the
  // outermost verifyAll block
  if (ErrorCollector.getCollectionMode() == ErrorCollectionMode.Soft) return assertions()
  ErrorCollector.setCollectionMode(ErrorCollectionMode.Soft)
  return assertions().apply {
    ErrorCollector.throwCollectedErrors()
  }
}

/**
 * Run multiple assertions and throw a single error after all are executed if any fail
 *
 * This method will run all the assertions inside [assertions] block, and will collect all failures that may happen.
 * It then compact all of them in a single throwable and throw it instead, or nothing if no assertion fail.
 *
 * ```
 *     // All assertions below are going to be executed, even when one or multiple fail.
 *     // All the failures are then collected and thrown in one single throwable.
 *     "foo".assertSoftly {
 *         this[2] shouldBe 'o'
 *         length shouldBeLessThan 5
 *     }
 * ```
 */
inline fun <T> T.assertSoftly(assertions: T.(T) -> Unit): T {
   return io.kotest.assertions.assertSoftly {
      assertions(this)
      this
   }
}
