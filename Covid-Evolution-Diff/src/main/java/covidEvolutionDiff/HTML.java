package covidEvolutionDiff;

import java.awt.Desktop;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import org.jsoup.*;
import org.jsoup.nodes.*;

public class HTML {

	private ArrayList<String> firstOneString = new ArrayList<String>();
	private ArrayList<String> secondOneString = new ArrayList<String>();

	public void toHTML(String firstOne, String secondOne) throws IOException {
		fillEachArray(firstOne, secondOne);

		File f = new File("source.html");
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));

		Document doc = Jsoup.parse("<html></html>");
		doc.body().addClass("body-styles-cls");
		doc.body().appendElement("div");

		writeHTML(doc);

		bw.write(doc.toString());
		bw.close();
		Desktop.getDesktop().browse(f.toURI());

	}

	private void fillEachArray(String firstOne, String secondOne) {

		Scanner scanner = new Scanner(firstOne);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			firstOneString.add(line);
		}
		scanner.close();

		Scanner scannerSecond = new Scanner(secondOne);
		while (scannerSecond.hasNextLine()) {
			String line = scannerSecond.nextLine();
			secondOneString.add(line);
		}
		scannerSecond.close();
	}

	private void writeHTML(Document doc) {

		previousCovid19SpreadingVersion(doc);

		currentCovid19SpreadingVersion(doc);

	}

	private void previousCovid19SpreadingVersion(Document doc) {
		doc.body().appendElement("<div style=\" float:left\">");

		for (int i = 0; i < secondOneString.size(); i++) {

			String str = secondOneString.get(i).replaceAll("<", "&lt;");
			str = str.replaceAll(">", "&gt;");

			if (secondOneString.get(i).equals(firstOneString.get(i)))
				doc.body().append(str + "<br>");
			else
				doc.body().append("<span style=\"background-color: #E74C3C\">" + str + "</span><br>");
		}
		doc.body().appendElement("</div>");

	}

	private void currentCovid19SpreadingVersion(Document doc) {
		doc.body().appendElement("<div style=\" float:right\">");

		for (int i = 0; i < firstOneString.size(); i++) {

			String str = firstOneString.get(i).replaceAll("<", "&lt;");
			str = str.replaceAll(">", "&gt;");

			if (firstOneString.get(i).equals(secondOneString.get(i)))
				doc.body().append(str + "<br>");
			else
				doc.body().append("<span style=\"background-color: #2ECC71\">" + str + "</span><br>");

		}
		doc.body().appendElement("</div>");

	}
}
