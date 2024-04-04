package jp.co.yumemi.android.code_check.view.searchBarAndListFragment

import android.view.KeyEvent
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressKey
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.view.NavHostActivity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchBarFragmentTest {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    private fun isUsableKeyCode(keyCode: Int): Boolean{
        when (keyCode) {
            in 7..16 -> return true
            in 29 .. 54 -> return true
            else -> return false
        }

    }
    @Test
    fun characterLimitationTest(){
        val scenario = launchActivity<NavHostActivity>()
        scenario.moveToState(Lifecycle.State.RESUMED)
        val searchInputText = onView(withId(R.id.search_input_text))
        searchInputText.perform(click(), pressKey(KeyEvent.KEYCODE_1), pressKey(KeyEvent.KEYCODE_APOSTROPHE))
        searchInputText.check(matches(withText("1")))
    }

    @Test
    fun upperCaseTest(){
        val scenario = launchActivity<NavHostActivity>()
        scenario.moveToState(Lifecycle.State.RESUMED)
        val searchInputText = onView(withId(R.id.search_input_text))
        searchInputText.perform(click(),pressKey(KeyEvent.KEYCODE_A), pressKey(KeyEvent.KEYCODE_SHIFT_LEFT))
        searchInputText.check(matches(withText("a")))
    }
}