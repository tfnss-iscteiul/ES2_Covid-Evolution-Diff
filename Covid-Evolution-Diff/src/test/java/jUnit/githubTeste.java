package jUnit;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import covidEvolutionDiff.Github;

class githubTeste {

	private static ArrayList<String> commitsWithTags = new ArrayList<String>();
	private static ArrayList<ObjectLoader> loaderArray = new ArrayList<ObjectLoader>();
	private static ArrayList<String> covid19spreadingArray = new ArrayList<String>();

	static Github gitExec = new Github();

	@SuppressWarnings("static-access")
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		Github.getInstance().runGit();
		commitsWithTags = Github.getInstance().getCommitsWithTags();
		loaderArray = Github.getInstance().getLoaderArray();
		covid19spreadingArray = Github.getInstance().getCovid19spreadingArray();
	}

	@Test
	@Order(1)
	void test() {
		// System.out.println(gitExecgetIterator);
		assertEquals(2, Github.getInstance().getIterator());
		assertEquals(commitsWithTags.size(), Github.getInstance().getCommitsWithTagsSize());
		assertEquals(covid19spreadingArray.size(), Github.getInstance().getCovid19spreadingArraySize());
		assertEquals(loaderArray.size(), Github.getInstance().getLoaderArraySize());
	}

	

}
