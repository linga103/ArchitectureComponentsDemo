/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.codingjam.github.ui.repo

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.support.v4.app.FragmentActivity
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import it.codingjam.github.NavigationController
import it.codingjam.github.repository.RepoRepository
import it.codingjam.github.util.TestData
import it.codingjam.github.util.TestLiveDataObserver
import it.codingjam.github.util.TrampolineSchedulerRule
import it.codingjam.github.util.shouldContain
import it.codingjam.github.vo.RepoId
import it.codingjam.github.willReturnSingle
import it.codingjam.github.willThrowSingle
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.junit.MockitoJUnit
import java.io.IOException

class RepoViewModelTest {

    @get:Rule var mockitoRule = MockitoJUnit.rule()

    @get:Rule var trampolineSchedulerRule = TrampolineSchedulerRule()

    @get:Rule var instantExecutorRule = InstantTaskExecutorRule()

    val repository: RepoRepository = mock()

    val navigationController: NavigationController = mock()

    val activity: FragmentActivity = mock()

    @InjectMocks lateinit var repoViewModel: RepoViewModel

    private val observer = TestLiveDataObserver<RepoViewState>()

    @Before fun setUp() {
        repoViewModel.observeForever({ it(activity) }, observer)
    }

    @Test fun fetchData() {
        given(repository.loadRepo("a", "b")) willReturnSingle { TestData.REPO_DETAIL }

        repoViewModel.init(RepoId("a", "b"))

        observer.values.map { it.repoDetail } shouldContain {
            empty().loading().success()
        }
    }

    @Test fun errorFetchingData() {
        given(repository.loadRepo("a", "b")) willThrowSingle { Throwable() }

        repoViewModel.init(RepoId("a", "b"))

        observer.values.map { it.repoDetail } shouldContain {
            empty().loading().error()
        }
    }

    @Test
    fun retry() {
        given(repository.loadRepo("a", "b"))
                .willThrowSingle { IOException() }
                .willReturnSingle { TestData.REPO_DETAIL }

        repoViewModel.init(RepoId("a", "b"))

        repoViewModel.retry()

        observer.values.map { it.repoDetail } shouldContain {
            empty().loading().error().loading().success()
        }
    }
}