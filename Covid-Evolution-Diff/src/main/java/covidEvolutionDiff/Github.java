package covidEvolutionDiff;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;


public class Github {
	public static int iterator = 0;
	private static ArrayList<String> commitsWithTags = new ArrayList<String>();
	private static ArrayList<ObjectLoader> loaderArray = new ArrayList<ObjectLoader>();
	private static ArrayList<String> covid19spreadingArray = new ArrayList<String>();
	private static HTML html = new HTML();

	private static String text;

	public static RevCommit begin, last;
	
	private static Github instance;


	public static void main(String[] args) throws Exception {
		runGit();
	}
	
	public static void runGit() throws IOException, InvalidRemoteException, TransportException, GitAPIException {
		File localPath = File.createTempFile("JGitTestRepository", "");
		Files.delete(localPath.toPath());

		Git git = Git.cloneRepository().setURI("https://github.com/vbasto-iscte/ESII1920").setDirectory(localPath)
				.call();

		Repository repository = git.getRepository();

		@SuppressWarnings("resource")
		List<Ref> call = new Git(repository).tagList().call();
		for (Ref ref : call) {

			commitsWithTags.add(ref.getObjectId().getName());
		}

		String treeName = "refs/heads/master"; // tag or branch
		for (RevCommit commit : git.log().add(repository.resolve(treeName)).call()) {

			for (String tagID : commitsWithTags) {
				if (iterator > 1)
					break;

				if (commit.getName().equals(tagID.toString())) {
					if (iterator == 1)
						begin = commit;

					if (iterator == 0)
						last = commit;

					findFile(commit, repository);
					iterator++;
				}
			}

		}
		// getDiff(repository, begin, last);
		html.toHTML(covid19spreadingArray.get(0), covid19spreadingArray.get(1));
	}

//	private static void getDiff(Repository repository, RevCommit begin, RevCommit last) throws Exception {
//
//		FileOutputStream stdout = new FileOutputStream(FileDescriptor.out);
//
//		try (DiffFormatter diffFormatter = new DiffFormatter(stdout)) {
//			diffFormatter.setRepository(repository);
//			for (DiffEntry entry : diffFormatter.scan(begin, last)) {
//				diffFormatter.format(diffFormatter.toFileHeader(entry));
//			}
//		}
//	}

	public static void findFile(RevCommit commit, Repository repository)
			throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try (TreeWalk treeWalk = new TreeWalk(repository)) {
			RevTree tree = commit.getTree();
			treeWalk.addTree(tree);
			treeWalk.setRecursive(true);
			treeWalk.setFilter(PathFilter.create("covid19spreading.rdf"));

			if (!treeWalk.next()) {
				throw new IllegalStateException("Did not find expected file 'covid19spreading.rdf'");

			}

			ObjectId objectId = treeWalk.getObjectId(0);
			ObjectLoader loader = repository.open(objectId);
			loaderArray.add(loader);

			loader.copyTo(baos);

			text = new String(baos.toByteArray());
			covid19spreadingArray.add(text);

		}

	}
	public static Github getInstance() {
		if (instance == null)
			instance = new Github();

		return instance;
	}
	
	public ArrayList<String> getCommitsWithTags() {
		return commitsWithTags;
	}

	public ArrayList<ObjectLoader> getLoaderArray() {
		return loaderArray;
	}

	public ArrayList<String> getCovid19spreadingArray() {
		return covid19spreadingArray;
	}

	public int getCommitsWithTagsSize() {
		return commitsWithTags.size();
	}

	public int getLoaderArraySize() {
		return loaderArray.size();
	}

	public int getCovid19spreadingArraySize() {
		return covid19spreadingArray.size();
	}
	
	public int getIterator() {
		return iterator;
	}
}
