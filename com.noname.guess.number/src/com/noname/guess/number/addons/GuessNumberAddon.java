 
package com.noname.guess.number.addons;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.osgi.service.datalocation.Location;

import com.noname.guess.number.core.GuessNumberGame;
import com.noname.guess.number.core.GuessNumberGameImpl;
import com.noname.guess.number.core.GuessNumberLevel;
import com.noname.guess.number.core.GuessNumberLevelImpl;
import com.noname.guess.number.core.dao.RatingDao;
import com.noname.guess.number.core.dao.RatingDaoImpl;

public class GuessNumberAddon {
	public static final String RATING_KEY = "rating";
	public static final String DEFAULT_LEVEL_KEY = "default";
	
	@PostConstruct
	public void postCostConstruct(IEclipseContext context,
			@SuppressWarnings("restriction") @Named(E4Workbench.INSTANCE_LOCATION) Location instanceLocation) throws UnsupportedEncodingException {
		GuessNumberLevel[] levels = new GuessNumberLevel[3];
		levels[0] = new GuessNumberLevelImpl("Easy", 0, 9);
		levels[1] = new GuessNumberLevelImpl("Medium", 0, 99);
		levels[2] = new GuessNumberLevelImpl("Hard", 0, 999);
		context.set(GuessNumberLevel[].class, levels);
		context.set(DEFAULT_LEVEL_KEY, levels[1]);

		GuessNumberGame game = new GuessNumberGameImpl();
		context.set(GuessNumberGame.class, game);
		
		URL url = instanceLocation.getURL();
		File workspaceDir = new File( URLDecoder.decode( url.getFile(), "UTF-8" ) );
		File ratingFile = new File(workspaceDir, "rating");
		RatingDao ratingDao = new RatingDaoImpl(ratingFile);
		context.set(RatingDao.class, ratingDao);
		
		Map<String, Integer> rating = ratingDao.deserialize();
		context.set(RATING_KEY, rating);
	}
}
