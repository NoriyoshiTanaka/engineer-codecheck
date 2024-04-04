package jp.co.yumemi.android.code_check.view


import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.model.IRepositorySearchDataSource
import jp.co.yumemi.android.code_check.model.dataClass.Item
import jp.co.yumemi.android.code_check.model.dataClass.Owner
import jp.co.yumemi.android.code_check.serviceLocator.RepositorySearchDataSourceLocator
import junit.framework.TestCase.assertEquals
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class NavHostActivityTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(NavHostActivity::class.java)

    /**
     * サーチバーに文字を入力して件s区を実行するテスト
     */
    @Test
    fun runThrough() {

        // 検索のキーとなる文字
        val expected = "a"

        // フェイクのデータソースを作成して、ServiceLocatorに注入する
        val fakeDataSource = object : IRepositorySearchDataSource {
            override suspend fun searchRepository(query: CharSequence): List<Item> {
                // サーチバーに入力した文字が届いたことを確認する
                assertEquals(expected, query.toString())

                // ダミーのデータを作成して返す
                return generateSearchResult(10)
            }
        }
        RepositorySearchDataSourceLocator.repositorySearchDataSource = fakeDataSource

        //
        val scenario = launchActivity<NavHostActivity>()
        //scenario.moveToState(Lifecycle.State.RESUMED)

        // expectedを入力して実行ボタンを押す
        val searchInputText = onView(withId(R.id.searchInputText))
        searchInputText.perform(click(), replaceText(expected))
        searchInputText.perform(pressImeActionButton())

        InstrumentationRegistry.getInstrumentation().waitForIdleSync()

        // "1"と表示された行が存在することを確認して、クリックする
        val row = onView(withText("1"))
        row.check(matches(isDisplayed()))
        row.perform(click())

        InstrumentationRegistry.getInstrumentation().waitForIdleSync()

        // 詳細画面へ遷移したことを確認する
        val resources = InstrumentationRegistry.getInstrumentation().targetContext
        val forkString = resources.getString(R.string.forks, 1)
        val languageString = resources.getString(R.string.written_language, "1")
        val starsString = resources.getString(R.string.stars, 1)
        val watcherString = resources.getString(R.string.watchers, 1)
        val openIssuesString = resources.getString(R.string.open_issues, 1)

        onView(allOf(withId(R.id.forksView), withText(forkString))).check(matches(isDisplayed()))
        onView(allOf(withId(R.id.languageView), withText(languageString))).check(matches(isDisplayed()))
        onView(allOf(withId(R.id.starsView), withText(starsString))).check(matches(isDisplayed()))
        onView(allOf(withId(R.id.watchersView), withText(watcherString))).check(matches(isDisplayed()))
        onView(allOf(withId(R.id.openIssuesView), withText(openIssuesString))).check(matches(isDisplayed()))
    }
}

/**
 * itemCount回ループしてダミーデータを作成する。
 * Stringのプロパティは(ループのカウント).toString、Intのプロパティはループのカウントが入っている。
 */

fun generateSearchResult(itemCount: Int): List<Item> {
    val mutableList = mutableListOf<Item>()
    for (count in 0 until itemCount) {
        mutableList.add(generateItem(count))
    }
    return mutableList.toList()
}

fun generateItem(number: Int): Item {
    val numberString = number.toString()
    return Item(
        forksCount = number,
        fullName = numberString,
        language = numberString,
        openIssuesCount = number,
        owner = Owner(avatarUrl = numberString),
        stargazersCount = number,
        watchersCount = number
    )
}

