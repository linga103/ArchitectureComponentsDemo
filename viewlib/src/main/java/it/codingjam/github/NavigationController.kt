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

package it.codingjam.github

import android.support.v4.app.FragmentActivity
import it.codingjam.github.core.RepoId

interface NavigationController {
    fun navigateToSearch(activity: FragmentActivity)
    fun navigateToRepo(activity: FragmentActivity, repoId: RepoId)
    fun navigateToUser(activity: FragmentActivity, login: String)
    fun showError(activity: FragmentActivity, error: String?)
}

