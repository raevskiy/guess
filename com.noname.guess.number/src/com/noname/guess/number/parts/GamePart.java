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
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
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

public class GamePart {

	private Composite introComposite;
	private Composite gameComposite;
	private Spinner spinnerGuessValue;
	private Button buttonStart;
	private Label labelOutcome;
	private Button buttonGuess;

	@Inject
	RatingDao rationDao;
	@Inject
	@Named(GuessNumberAddon.RATING_KEY)
	private Map<String, Integer> rating;
	@Inject
	private GuessNumberGame game;
	@Inject
	private GuessNumberLevel[] levels;
	private GuessNumberLevel selectedLevel;

	@PostConstruct
	public void createComposite(Composite parent, EPartService partService) {
		game.addGameEventListener(new GameEventListenerImpl(parent));

		parent.setLayout(new GridLayout(1, false));

		introComposite = new Composite(parent, SWT.NONE);
		introComposite.setLayout(new GridLayout(1, false));
		GridData data = new GridData(GridData.FILL_BOTH);
		data.exclude = false;
		introComposite.setLayoutData(data);

		gameComposite = new Composite(parent, SWT.NONE);
		gameComposite.setLayout(new GridLayout(2, false));
		data = new GridData(GridData.FILL_BOTH);
		data.exclude = true;
		gameComposite.setLayoutData(data);
		gameComposite.setVisible(false);

		// Create the first Group
		Group groupLevel = new Group(introComposite, SWT.SHADOW_IN);
		groupLevel.setText("Difficulty level");
		groupLevel.setLayout(new RowLayout(SWT.VERTICAL));
		for (final GuessNumberLevel level : levels) {
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

			if (level.isDefault()) {
				buttonLevel.setSelection(true);
				selectedLevel = level;
			}
		}
		groupLevel.setLayoutData(new GridData(GridData.FILL_BOTH));

		buttonStart = new Button(introComposite, SWT.PUSH);
		buttonStart.setText("Start");
		buttonStart.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		buttonStart.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				game.start(selectedLevel);
			}
		});
		buttonStart.setEnabled(selectedLevel != null);

		spinnerGuessValue = new Spinner(gameComposite, SWT.NONE);
		spinnerGuessValue.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		spinnerGuessValue.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent event) {
				try {
					Integer newInteger = Integer.parseInt(spinnerGuessValue.getText());
					if (newInteger <= spinnerGuessValue.getMaximum() && newInteger >= spinnerGuessValue.getMinimum()) {
						labelOutcome.setText("");
						buttonGuess.setEnabled(true);
					} else {
						StringBuilder sb = new StringBuilder("Please use a value in the the range");
						addBoundaries(sb, selectedLevel);
						labelOutcome.setText(sb.toString());
						buttonGuess.setEnabled(false);
					}
				} catch (NumberFormatException e) {
					//NOP
				}
			}
		});

		labelOutcome = new Label(gameComposite, SWT.NONE);
		labelOutcome.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		labelOutcome.setText("");

		buttonGuess = new Button(gameComposite, SWT.PUSH);
		buttonGuess.setText("Try this");
		buttonGuess.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		buttonGuess.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				int supposedValue = spinnerGuessValue.getSelection();
				int outcome = game.guess(supposedValue);
				if (outcome < 0) {
					labelOutcome.setText("The number is greater then " + supposedValue);
				} else if (outcome > 0) {
					labelOutcome.setText("The number is less then " + supposedValue);
				} else {
					Shell activeShell = Display.getCurrent().getActiveShell();
					InputDialog dlg = new InputDialog(activeShell,
							"Well done. Total attempts: " + game.getAttempts(),
							"Enter your name",
							"Player",
							new LengthValidator());
					if (dlg.open() == Window.OK) {
						String playerName = dlg.getValue();
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
						rating.put(playerName, newRating);
						try {
							rationDao.serialize(rating);
						} catch (IOException e) {
							MultiStatus status = createMultiStatus(e.getLocalizedMessage(), e);
							 ErrorDialog.openError(
									 activeShell,
									 "Error",
									 "Failed to save your rating",
									 status);
						}
						
						MPart ratingPart = partService.findPart("com.noname.guess.number.rating");
						if (ratingPart != null) {
							((RatingPart)ratingPart.getObject()).update(rating);
							partService.activate(ratingPart);	
						}
					}
				}
			}
		});

		Button buttonSurrender = new Button(gameComposite, SWT.PUSH);
		buttonSurrender.setText("Surrender");
		buttonSurrender.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		buttonSurrender.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				game.cancel();
			}
		});

	}

	private void addBoundaries(StringBuilder sb, GuessNumberLevel level) {
		sb.append(" [");
		sb.append(level.getLowerBound());
		sb.append(", ");
		sb.append(level.getUpperBound());
		sb.append("]");
	}

	@Focus
	public void setFocus() {
	}

	@Persist
	public void save() {
	}
	
	private static MultiStatus createMultiStatus(String msg, Throwable t) {

	    List<Status> childStatuses = new ArrayList<>();
	    StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();

	     for (StackTraceElement stackTrace: stackTraces) {
	      Status status = new Status(IStatus.ERROR,
	          "com.example.e4.rcp.todo", stackTrace.toString());
	      childStatuses.add(status);
	    }

	    MultiStatus ms = new MultiStatus("com.example.e4.rcp.todo",
	        IStatus.ERROR, childStatuses.toArray(new Status[] {}),
	        t.toString(), t);
	    return ms;
	  }

	private class GameEventListenerImpl implements GameEventListener {
		private final Composite parentComposite;

		public GameEventListenerImpl(Composite parentComposite) {
			this.parentComposite = parentComposite;
		}

		private void manageCompositeVisibility(boolean introVisible, boolean gameVisible) {
			introComposite.setVisible(introVisible);
			((GridData) introComposite.getLayoutData()).exclude = !introVisible;

			gameComposite.setVisible(gameVisible);
			((GridData) gameComposite.getLayoutData()).exclude = !gameVisible;

			parentComposite.layout(false);
		}

		@Override
		public void onGameStrated() {
			int lowerBound = selectedLevel.getLowerBound();
			spinnerGuessValue.setMinimum(lowerBound);
			spinnerGuessValue.setMaximum(selectedLevel.getUpperBound());
			spinnerGuessValue.setSelection(lowerBound);

			manageCompositeVisibility(false, true);
		}

		@Override
		public void onGameStopped() {
			manageCompositeVisibility(true, false);
		}
	}

	private class LengthValidator implements IInputValidator {
		@Override
		public String isValid(String newText) {
			int len = newText.length();
			if (len < 1)
				return "Too short";
			if (len > 20)
				return "Too long";
			return null;
		}
	}
}