 
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
import com.noname.guess.number.i18n.Messages;

@SuppressWarnings("restriction")
public class GuessNumberAddon {
	public static final String RATING_KEY = "rating"; //$NON-NLS-1$
	
	@PostConstruct
	public void postCostConstruct(
			IEclipseContext context,
			@Named(E4Workbench.INSTANCE_LOCATION) Location instanceLocation) throws UnsupportedEncodingException {
		GuessNumberLevel[] levels = new GuessNumberLevel[3];
		levels[0] = new GuessNumberLevelImpl(Messages.GuessNumberAddon_LevelEasy, 0, 9);
		levels[1] = new GuessNumberLevelImpl(Messages.GuessNumberAddon_LevelMedium, 0, 99);
		levels[2] = new GuessNumberLevelImpl(Messages.GuessNumberAddon_LevelHard, 0, 999);
		context.set(GuessNumberLevel[].class, levels);

		GuessNumberGame game = new GuessNumberGameImpl();
		context.set(GuessNumberGame.class, game);
		
		URL url = instanceLocation.getURL();
		File workspaceDir = new File( URLDecoder.decode( url.getFile(), "UTF-8" ) ); //$NON-NLS-1$
		File ratingFile = new File(workspaceDir, "rating"); //$NON-NLS-1$
		RatingDao ratingDao = new RatingDaoImpl(ratingFile);
		context.set(RatingDao.class, ratingDao);
		
		Map<String, Integer> rating = ratingDao.deserialize();
		context.set(RATING_KEY, rating);
	}
}
