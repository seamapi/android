package co.seam.seamcomponents

/**
 * Controls whether the unlock card attempts an unlock outside the key's validity period.
 */
object SeamUnlockPolicy {
    /**
     * When `true`, the unlock card refuses to start an unlock before the key's start date or after
     * its checkout date, and explains why. Defaults to `false`, which leaves the decision to the lock.
     */
    @Volatile
    var blocksUnlockOutsideValidPeriod: Boolean = false
}
