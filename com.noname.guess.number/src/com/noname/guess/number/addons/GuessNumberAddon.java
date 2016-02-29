 
package com.noname.guess.number.addons;

import javax.annotation.PostConstruct;

import org.eclipse.e4.core.contexts.IEclipseContext;

import com.noname.guess.number.core.GuessNumberGame;
import com.noname.guess.number.core.GuessNumberGameImpl;
import com.noname.guess.number.core.GuessNumberLevel;
import com.noname.guess.number.core.GuessNumberLevelImpl;

public class GuessNumberAddon {
	@PostConstruct
	public void postCostConstruct(IEclipseContext context) {
		GuessNumberLevel[] levels = new GuessNumberLevel[3];
		levels[0] = new GuessNumberLevelImpl("Easy", 0, 9);
		levels[1] = new GuessNumberLevelImpl("Medium", 0, 99, true);
		levels[2] = new GuessNumberLevelImpl("Hard", 0, 999);
		context.set(GuessNumberLevel[].class, levels);
		
		GuessNumberGame game = new GuessNumberGameImpl();
		context.set(GuessNumberGame.class, game);
	}
}
