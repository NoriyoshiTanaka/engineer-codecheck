package jp.co.yumemi.android.code_check.viewModel

import jp.co.yumemi.android.code_check.model.RepositorySearchDataSource
import jp.co.yumemi.android.code_check.model.dataClass.Item
import jp.co.yumemi.android.code_check.model.dataClass.Owner
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class RepositorySearchViewModelTest {

    private lateinit var mockDataSource: RepositorySearchDataSource
    private fun getViewModel(): RepositorySearchViewModel{
        return RepositorySearchViewModel(mockDataSource)
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        mockDataSource = Mockito.mock(RepositorySearchDataSource::class.java)
    }

    /**
     * RepositorySearchViewModel#searchRepositoryへ渡した引数がRepositorySearchDataSource#searchRepositoryの
     * 引数として渡されたことを確認する。
     *
     * RepositorySearchDataSourceから受けとったデータがFlowへ格納されたことを確認する
     *
     * Flowに格納されたデータを検証する
     */
    @Test
    fun executeSearchTest() = runTest {
        val result = generateSearchResult(10)
        val viewModel = getViewModel()
        val dispatcher = StandardTestDispatcher(testScheduler)
        val query = "query"

        // 検索したら10個分のデータを返すように設定留守
        `when`(mockDataSource.searchRepository(query)).thenReturn(result)

        // 検索を実行する
        viewModel.searchRepository(query, dispatcher)

        advanceUntilIdle()

        // ここから検証
        // 所定の文字列で検索されたことを確認する
        verify(mockDataSource, times(1)).searchRepository(query)

        // Flowに格納されたListのsizeを確認する
        assertEquals(10, viewModel.repositoryListFlow.value.size)

        // Listの最初のアイテムを確認する
        val firstItem = viewModel.findRepository("0")
        assertEquals(result[0], firstItem)

        // fullName == "11" は存在しないので null であることを確認する
        val nullItem = viewModel.findRepository("10")
        assertEquals(null, nullItem)
    }

    /**
     * itemCount回ループしてダミーデータを作成する。
     * Stringのプロパティは(ループのカウント).toString、Intのプロパティはループのカウントが入っている。
     */
    private fun generateSearchResult(itemCount: Int): List<Item> {
        val mutableList = mutableListOf<Item>()
        for (count in 0 until itemCount) {
            mutableList.add(generateItem(count))
        }
        return  mutableList.toList()
    }
    private fun generateItem(number: Int): Item {
        val numberString = number.toString()
        return Item(
            forksCount = number,
            fullName = numberString,
            language = null,
            openIssuesCount = number,
            owner = Owner(avatarUrl = numberString),
            stargazersCount = number,
            watchersCount = number
        )
    }
}