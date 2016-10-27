/*******************************************************************************
 * Copyright (c) 2010 - 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Lars Vogel <lars.Vogel@gmail.com> - Bug 419770
 *******************************************************************************/
package com.noname.guess.number.parts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

import com.noname.guess.number.addons.GuessNumberAddon;
import com.noname.guess.number.core.GameEventListener;
import com.noname.guess.number.core.GuessNumberGame;
import com.noname.guess.number.core.GuessNumberLevel;
import com.noname.guess.number.core.dao.RatingDao;
import com.noname.guess.number.i18n.Messages;
import com.noname.guess.number.parts.jface.PlayerNameValidator;

public class GamePart {
	private Composite introComposite;
	private Composite gameComposite;
	private Spinner spinnerGuessValue;
	private Button buttonStart;
	private Label labelOutcome;
	private Button buttonGuess;

	@Inject
	EPartService partService;
	@Inject
	RatingDao ratingDao;
	@Inject
	@Named(GuessNumberAddon.RATING_KEY)
	private Map<String, Integer> rating;
	@Inject
	private GuessNumberGame game;
	private GuessNumberLevel selectedLevel;

	@PostConstruct
	public void createComposite(
			Composite parent,
			GuessNumberLevel[] levels) {
		game.addGameEventListener(new GameEventListenerImpl(parent));

		parent.setLayout(new GridLayout(1, false));
		createIntroComposite(parent, levels);
		createGameComposite(parent);
	}
	
	private void createIntroComposite(Composite parent, GuessNumberLevel[] levels) {
		introComposite = new Composite(parent, SWT.NONE);
		introComposite.setLayout(new GridLayout(1, false));
		GridData data = new GridData(GridData.FILL_BOTH);
		data.exclude = false;
		introComposite.setLayoutData(data);
		
		createSelectLevelGroup(levels);
		createStartButton();
	}
	
	private void createGameComposite(Composite parent) {
		gameComposite = new Composite(parent, SWT.NONE);
		gameComposite.setLayout(new GridLayout(2, false));
		GridData data = new GridData(GridData.FILL_BOTH);
		data.exclude = true;
		gameComposite.setLayoutData(data);
		gameComposite.setVisible(false);
		
		createGuessValueSpinner();
		createLabelOutcome();
		createButtonGuess();
		createButtonSurrender();
	}
	
	private void createSelectLevelGroup(GuessNumberLevel[] levels) {
		Group groupLevel = new Group(introComposite, SWT.SHADOW_IN);
		groupLevel.setText(Messages.GamePart_Difficulty);
		groupLevel.setLayout(new RowLayout(SWT.VERTICAL));
		for (int i = 0; i < levels.length; i++) {
			GuessNumberLevel level = levels[i];
			Button buttonLevel = new Button(groupLevel, SWT.RADIO);
			StringBuilder levelDescription = new StringBuilder(level.getName());
			addBoundaries(levelDescription, level);
			buttonLevel.setText(levelDescription.toString());
			buttonLevel.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					selectedLevel = level;
					if (!buttonStart.isEnabled())
						buttonStart.setEnabled(true);
				}
			});

			if (i == 0) {
				buttonLevel.setSelection(true);
				selectedLevel = level;
			}
		}
		groupLevel.setLayoutData(new GridData(GridData.FILL_BOTH));
	}
	
	private void createStartButton() {
		buttonStart = new Button(introComposite, SWT.PUSH);
		buttonStart.setText(Messages.GamePart_StartGame);
		buttonStart.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		buttonStart.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				game.start(selectedLevel);
			}
		});
		buttonStart.setEnabled(selectedLevel != null);
	}
	
	private void createGuessValueSpinner() {
		spinnerGuessValue = new Spinner(gameComposite, SWT.NONE);
		spinnerGuessValue.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		spinnerGuessValue.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent event) {
				try {
					Integer newInteger = Integer.parseInt(spinnerGuessValue.getText());
					if (newInteger <= spinnerGuessValue.getMaximum() && newInteger >= spinnerGuessValue.getMinimum()) {
						labelOutcome.setText(""); //$NON-NLS-1$
						buttonGuess.setEnabled(true);
					} else {
						StringBuilder sb = new StringBuilder(Messages.GamePart_RangeHint);
						addBoundaries(sb, selectedLevel);
						labelOutcome.setText(sb.toString());
						buttonGuess.setEnabled(false);
					}
				} catch (NumberFormatException e) {
					// NOP
				}
			}
		});
	}
	
	private void createLabelOutcome() {
		labelOutcome = new Label(gameComposite, SWT.NONE);
		labelOutcome.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		labelOutcome.setText(""); //$NON-NLS-1$
	}
	
	private void createButtonGuess() {
		buttonGuess = new Button(gameComposite, SWT.PUSH);
		buttonGuess.setText(Messages.GamePart_TryNumber);
		buttonGuess.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		buttonGuess.addSelectionListener(new GuessSelectionAdapter());
	}
	
	private void createButtonSurrender() {
		Button buttonSurrender = new Button(gameComposite, SWT.PUSH);
		buttonSurrender.setText(Messages.GamePart_Surrender);
		buttonSurrender.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		buttonSurrender.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int number = game.cancel();
				MessageDialog.openInformation(Display.getCurrent().getActiveShell(), Messages.GamePart_GameCancelled,
						Messages.GamePart_RightAnswer + number);
			}
		});
	}

	private void addBoundaries(StringBuilder sb, GuessNumberLevel level) {
		sb.append(" ["); //$NON-NLS-1$
		sb.append(level.getLowerBound());
		sb.append(", "); //$NON-NLS-1$
		sb.append(level.getUpperBound());
		sb.append("]"); //$NON-NLS-1$
	}

	@Focus
	public void setFocus() {
	}

	@Persist
	public void save() {
	}

	private class GameEventListenerImpl implements GameEventListener {
		private final Composite parentComposite;

		public GameEventListenerImpl(Composite parentComposite) {
			this.parentComposite = parentComposite;
		}

		@Override
		public void onGameStarted() {
			int lowerBound = selectedLevel.getLowerBound();
			spinnerGuessValue.setMinimum(lowerBound);
			spinnerGuessValue.setMaximum(selectedLevel.getUpperBound());
			spinnerGuessValue.setSelection(lowerBound);
			manageCompositeVisibility();
		}

		@Override
		public void onGameStopped() {
			manageCompositeVisibility();
		}
		
		private void manageCompositeVisibility() {
			boolean inProgress = game.isInProgress();
			introComposite.setVisible(!inProgress);
			((GridData) introComposite.getLayoutData()).exclude = inProgress;

			gameComposite.setVisible(inProgress);
			((GridData) gameComposite.getLayoutData()).exclude = !inProgress;

			parentComposite.layout(false);
		}
	}

	private class GuessSelectionAdapter extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent event) {
			int supposedValue = spinnerGuessValue.getSelection();
			int outcome = game.guess(supposedValue);
			if (outcome < 0) {
				labelOutcome.setText(Messages.GamePart_Greater + supposedValue);
			} else if (outcome > 0) {
				labelOutcome.setText(Messages.GamePart_Less + supposedValue);
			} else {
				onGameWon();
			}
		}
		
		private void onGameWon() {
			Shell activeShell = Display.getCurrent().getActiveShell();
			InputDialog dlg = new InputDialog(
					activeShell,
					Messages.GamePart_VictoryTitle + game.getAttempts(),
					Messages.GamePart_InputPlayerName,
					Messages.GamePart_DefaultPlayerName,
					new PlayerNameValidator());
			if (dlg.open() == Window.OK) {
				showRating(dlg.getValue());
			}
		}
		
		private void showRating(String playerName) {
			updateRating(playerName);
			MPart ratingPart = partService.findPart("com.noname.guess.number.rating"); //$NON-NLS-1$
			if (ratingPart != null) {
				partService.activate(ratingPart);
				((RatingPart)ratingPart.getObject()).update(rating);
			}
		}
		
		private void updateRating(String playerName) {
			rating.put(playerName, calculateNewRating(playerName));
			
			try {
				ratingDao.serialize(rating);
			} catch (IOException e) {
				MultiStatus status = createMultiStatus(e.getLocalizedMessage(), e);
				ErrorDialog.openError(
						Display.getCurrent().getActiveShell(),
						Messages.GamePart_SerializeErrorTitle,
						Messages.GamePart_SerializeErrorMessage,
						status);
			}
		}
		
		private int calculateNewRating(String playerName) {
			Integer oldRating = rating.get(playerName);
			if (oldRating == null) {
				oldRating = 0;
			}

			int newRating;
			try {
				newRating = Math.addExact(oldRating, game.getRating());
			} catch (ArithmeticException e) {
				newRating = Integer.MAX_VALUE;
			}
			return newRating;
		}
		
		private MultiStatus createMultiStatus(String msg, Throwable t) {
			List<Status> childStatuses = new ArrayList<>();
			StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
			String pluginId = "com.noname.guess.number"; //$NON-NLS-1$

			for (StackTraceElement stackTrace : stackTraces) {
				Status status = new Status(
						IStatus.ERROR,
						pluginId, 
						stackTrace.toString());
				childStatuses.add(status);
			}

			MultiStatus ms = new MultiStatus(
					pluginId,
					IStatus.ERROR,
					childStatuses.toArray(new Status[] {}),
					t.toString(),
					t);
			return ms;
		}
	}
}