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
	
	@Test
	public void shouldWinProperly() {
		game.start(level);
		
		int outcome = game.guess(MOCKED_NUMBER);
		
		assertEquals(outcome, 0);
		assertEquals(game.getRating(), RATING_MAX);
		assertFalse(game.isInProgress());
	}
	
	@Test
	public void shouldCancelProperly() {
		game.start(level);
		
		int number = game.cancel();
		
		assertEquals(number, MOCKED_NUMBER);
		assertEquals(game.getRating(), 0);
		assertFalse(game.isInProgress());
	}
	
	@Test
	public void shouldGiveNegativeHintIfGuessIsLessThanNumber() {
		game.start(level);
		
		int outcome = game.guess(25);
		
		assertTrue(outcome < 0);
		assertTrue(game.isInProgress());
	}
	
	@Test
	public void shouldGivePositiveHintIfGuessIsGreaterThanNumber() {
		game.start(level);
		
		int outcome = game.guess(75);
		
		assertTrue(outcome > 0);
		assertTrue(game.isInProgress());
	}
	
	@Test
	public void shouldApplyPenaltiesToRatingForMoreThanOneAttempt() {
		game.start(level);
		//Kind of bruteforce
		for (int i = 0; i < RATING_MAX; i++) {
			game.guess(25);	
		}
		assertTrue(game.getRating() > 0);
		assertTrue(game.getRating() < RATING_MAX);
	}
	
	@Test
	public void shouldCallListenersOnGameStarted() {
		GameEventListener listener = mock(GameEventListener.class);
		game.addGameEventListener(listener);
		
		game.start(level);
		
		verify(listener, times(1)).onGameStarted();
	}
	
	@Test
	public void shouldCallListenersOnlyOnceOnGameStopped() {
		GameEventListener listener = mock(GameEventListener.class);
		game.addGameEventListener(listener);
		
		game.start(level);
		game.cancel();
		game.cancel();
		
		verify(listener, times(1)).onGameStopped();
	}
	
	@Test
	public void shouldNotCallRemovedListeners() {
		GameEventListener listener = mock(GameEventListener.class);
		game.addGameEventListener(listener);
		
		game.removeGameEventListener(listener);
		game.start(level);

		verify(listener, times(0)).onGameStarted();
	}

	@Test
	public void gameCallListeneresOnGameRestarted() {
		GameEventListener listener = mock(GameEventListener.class);
		game.addGameEventListener(listener);
		
		game.start(level);
		game.start(level);
		
		verify(listener, times(2)).onGameStarted();
		verify(listener, times(1)).onGameStopped();
	}
}
