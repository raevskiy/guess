package com.noname.guess.number.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.noname.guess.number.core.random.RandomGenerator;

public class GuessNumberGameImplTest {
	private static final int LOWER_BOUND = 0;
	private static final int UPPER_BOUND = 100;
	private static final int RATING_MAX = UPPER_BOUND - LOWER_BOUND;
	private static final int MOCKED_NUMBER = 50;
	private GuessNumberGame game;
	private GuessNumberLevel level = new GuessNumberLevelImpl("Test", LOWER_BOUND, UPPER_BOUND);
	
	@Before
	public void setUp() throws Exception {
		RandomGenerator generator = mock(RandomGenerator.class);
		when(generator.generateRandomInt(LOWER_BOUND, UPPER_BOUND)).thenReturn(MOCKED_NUMBER);
		game = new GuessNumberGameImpl(generator);
	}
	
	private void assertStoopped() {
		assertEquals(game.isInProgress(), false);
	}
	
	@Test
	public void gameIsWonProperly() {
		game.start(level);
		int outcome = game.guess(MOCKED_NUMBER);
		assertEquals(outcome, 0);
		assertEquals(game.getRating(), RATING_MAX);
		assertStoopped();
	}
	
	@Test
	public void gameIsCanceledProperly() {
		game.start(level);
		int outcome = game.cancel();
		assertEquals(outcome, MOCKED_NUMBER);
		assertEquals(game.getRating(), 0);
		assertStoopped();
	}
	
	@Test
	public void gameGivesHintsAndPenalties() {
		game.start(level);
		int outcome = game.guess(25);
		assertTrue(outcome < 0);
		outcome = game.guess(75);
		assertTrue(outcome > 0);
		assertEquals(game.isInProgress(), true);
		assertTrue(game.getRating() < RATING_MAX);
		game.cancel();
		assertStoopped();
	}
	
	@Test
	public void victoryYieldsPositiveRating() {
		game.start(level);
		//Kind of bruteforce
		for (int i = 0; i < RATING_MAX; i++) {
			game.guess(25);	
		}
		game.guess(MOCKED_NUMBER);
		assertTrue(game.getRating() > 0);
		assertStoopped();

	}
	
	@Test
	public void listenerIsCalled() {
		GameEventListener listener = mock(GameEventListener.class);
		game.addGameEventListener(listener);
		game.start(level);
		game.cancel();
		verify(listener, times(1)).onGameStarted();
		verify(listener, times(1)).onGameStopped();
		assertStoopped();
	}
	
	@Test
	public void listenerIsRemoved() {
		GameEventListener listener = mock(GameEventListener.class);
		game.addGameEventListener(listener);
		game.start(level);
		game.removeGameEventListener(listener);
		game.cancel();
		verify(listener, times(1)).onGameStarted();
		verify(listener, times(0)).onGameStopped();
		assertStoopped();
	}

	@Test
	public void gameCanBeRestarted() {
		GameEventListener listener = mock(GameEventListener.class);
		game.addGameEventListener(listener);
		game.start(level);
		game.start(level);
		verify(listener, times(2)).onGameStarted();
		verify(listener, times(1)).onGameStopped();
		game.cancel();
		assertStoopped();
	}
	
	@Test
	public void stoppedGameCannotBeCanceled() {
		int catchedExceptions = 0;
		
		try {
			game.cancel();
		} catch (IllegalStateException e) {
			catchedExceptions++;
		}
		
		try {
			game.start(level);
			game.guess(MOCKED_NUMBER);
			game.cancel();
		} catch (IllegalStateException e) {
			catchedExceptions++;
		}

		assertEquals(catchedExceptions, 2);
		assertStoopped();
	}
}
