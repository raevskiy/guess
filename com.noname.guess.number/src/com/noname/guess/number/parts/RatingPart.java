
package com.noname.guess.number.parts;

import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import com.noname.guess.number.addons.GuessNumberAddon;
import com.noname.guess.number.core.Player;
import com.noname.guess.number.i18n.Messages;

public class RatingPart {
	private TableViewer tableViewerRating;
	
	@PostConstruct
	public void postConstruct(
			Composite parent,
			@Named(GuessNumberAddon.RATING_KEY) Map<String, Integer> rating) {
		parent.setLayout(new FillLayout());
		
		tableViewerRating = new TableViewer(parent);
		tableViewerRating.setContentProvider(ArrayContentProvider.getInstance());
		final Table table = tableViewerRating.getTable();
	    table.setHeaderVisible(true);
	    table.setLinesVisible(true);
	    createNameColumn();
	    createRatingColumn();
		
		update(rating);
	}
	
	private void createNameColumn() {
		TableViewerColumn colName = new TableViewerColumn(tableViewerRating, SWT.NONE);
		colName.getColumn().setWidth(200);
		colName.getColumn().setText(Messages.RatingPart_PlayerName);
		colName.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Player p = (Player) element;
				return p.getName();
			}
		});
	}
	
	private void createRatingColumn() {
		TableViewerColumn colRating = new TableViewerColumn(tableViewerRating, SWT.NONE);
		colRating.getColumn().setWidth(200);
		colRating.getColumn().setText(Messages.RatingPart_PlayerRating);
		colRating.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Player p = (Player) element;
				return Integer.toString(p.getRating());
			}
		});
	}
	
	public void update(Map<String, Integer> rating) {
		SortedSet<Player> sortedRating = new TreeSet<Player>();
		for (Entry<String, Integer> entry : rating.entrySet()) {
			Player player = new Player(entry.getKey(), entry.getValue());
			sortedRating.add(player);
		}
		tableViewerRating.setInput(sortedRating);
	}
}