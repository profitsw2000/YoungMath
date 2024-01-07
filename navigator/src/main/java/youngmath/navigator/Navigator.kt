package youngmath.navigator

import android.os.Bundle

interface Navigator {

    fun navigateToMultiplicationScreen()

    fun navigateToMultiplicationTest()

    fun navigateToMultiplicationTestResult()

    fun navigateToMultiplicationHistory()

    fun navigateToMultiplicationHistoryDetails(bundle: Bundle)

    fun navigateToMultiplicationSettings()

    fun navigateUp()

}