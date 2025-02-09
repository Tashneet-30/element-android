/*
 * Copyright (c) 2023 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.vector.app.features.roomprofile.polls.list.domain

import im.vector.app.features.roomprofile.polls.list.data.RoomPollRepository
import im.vector.app.features.roomprofile.polls.list.ui.PollSummary
import im.vector.app.test.fixtures.RoomPollFixture
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

class GetPollsUseCaseTest {
    private val fakeRoomPollRepository = mockk<RoomPollRepository>()

    private val getPollsUseCase = GetPollsUseCase(
            roomPollRepository = fakeRoomPollRepository,
    )

    @Test
    fun `given repo when execute then correct method of repo is called and polls are sorted most recent first`() = runTest {
        // Given
        val aRoomId = "roomId"
        val poll1 = RoomPollFixture.anActivePollSummary(timestamp = 1)
        val poll2 = RoomPollFixture.anActivePollSummary(timestamp = 2)
        val poll3 = RoomPollFixture.anActivePollSummary(timestamp = 3)
        val polls = listOf<PollSummary>(
                poll1,
                poll2,
                poll3,
        )
        every { fakeRoomPollRepository.getPolls(aRoomId) } returns flowOf(polls)
        val expectedPolls = listOf<PollSummary>(
                poll3,
                poll2,
                poll1,
        )
        // When
        val result = getPollsUseCase.execute(aRoomId).first()

        // Then
        result shouldBeEqualTo expectedPolls
        verify { fakeRoomPollRepository.getPolls(aRoomId) }
    }
}
