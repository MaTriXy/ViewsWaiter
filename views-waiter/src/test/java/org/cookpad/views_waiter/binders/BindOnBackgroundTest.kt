package org.cookpad.views_waiter.binders

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.common.truth.Truth.assertThat
import org.cookpad.views_waiter.BuildConfig
import org.cookpad.views_waiter.R
import org.cookpad.views_waiter.ViewsWaiter
import org.cookpad.views_waiter.bindOnBackground
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = [21])
class BindOnBackgroundTest {

    @Test
    fun verifyLifeCycle() {
        val controller = Robolectric.buildActivity(ActivityTest::class.java).create()
        val activityTest = controller.get()

        controller.resume()
        pipelineCount.emit(1)
        assertThat(activityTest.count).isEqualTo(0)

        controller.pause()
        pipelineCount.emit(1)
        assertThat(activityTest.count).isEqualTo(1)

        controller.resume()
        pipelineCount.emit(2)
        assertThat(activityTest.count).isEqualTo(1)

        controller.pause()
        pipelineCount.emit(2)
        assertThat(activityTest.count).isEqualTo(2)
    }

    companion object {
        val pipelineCount = ViewsWaiter<Int>()
    }

    class ActivityTest : AppCompatActivity() {
        var count = 0

        override fun onCreate(savedInstanceState: Bundle?) {
            setTheme(R.style.Theme_AppCompat)
            super.onCreate(savedInstanceState)

            pipelineCount.stream()
                    .bindOnBackground(lifecycle)
                    .subscribe { updatedCount ->
                        count = updatedCount
                    }
        }
    }
}