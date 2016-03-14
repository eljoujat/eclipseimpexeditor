package org.com.test.csv;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dummy {

	final static String INSERT_UPDATE = "insert_update";
	final static String INSERT = "insert";
	final static String UPDATE = "update";
	final static String REMOVE = "remove";

	public static void main(String[] args) throws IOException {

		// String[] entries = "INSERT_UPDATE
		// PageTemplate;$contentCV[unique=true];uid[unique=true];name;frontendTemplateName;restrictedPageTypes(code);active[default=true];;MobileProductDetailsPageTemplate;Mobile
		// Product Details Page
		// Template;product/productLayout1Page;ProductPage;;MobileProductListPageTemplate;Mobile
		// Product List Page Template;category/productListPage;CategoryPage"
		// .split(";");





		String content = new Scanner(new File("poc_out.impex")).useDelimiter("\\Z").next();
		content=content.replace("\n", "");
		String[] entries = content.split(";");
		//		EntryBuilder entryBuilder = new EntryBuilder(new ImpexDataDefinition());
		//
		//		LinkedList<EntryData> entriesData = new LinkedList<>();
		//		int nbrOfEntriePerLine = 0;
		//		for (String rawEntry : entries) {
		//			EntryData entryData = entryBuilder.buildEntryData(rawEntry,"");
		//			nbrOfEntriePerLine = entryData.isHeader() ? nbrOfEntriePerLine + 1 : nbrOfEntriePerLine;
		//			entriesData.add(entryData);
		//		}
		//
		//
		//
		//		ImpexBlocBuilder blocBuilder = new ImpexBlocBuilder();
		//		int nbrOfLines= entriesData.size() / (nbrOfEntriePerLine-1);
		//		ImpexBloc impexBloc = blocBuilder.buildImpexBloc(entriesData, nbrOfEntriePerLine-1,nbrOfLines);
		//		System.out.println(impexBloc);


		// for (String entry : entries) {
		// if (isHeaderColumn(entry)) {
		// headerColumns.add(entry);
		// }
		//
		// }
		//
		// String[][] arragnedEntris = new String[2][headerColumns.size()];
		//
		// for (int i = 0; i < headerColumns.size(); i++) {
		// arragnedEntris[0][i] = headerColumns.get(i);
		// }
		//
		// int j = 1;
		// for (int i = headerColumns.size(); j < headerColumns.size(); j++) {
		// arragnedEntris[1][j] = entries[j + i];
		// }
		//
		// for (int i = 0; i < 2; i++) {
		// if (i == 1) {
		// System.out.print("\t\t\t\t");
		// }
		// for (int k = 0; k < headerColumns.size(); k++) {
		// String entry = (arragnedEntris[i][k] == null || arragnedEntris[i][k]
		// == "") ? "\t"
		// : arragnedEntris[i][k];
		// System.out.print(entry + ";\t");
		// }
		// System.out.print("\n");
		//
		// }

		String regexp = "^\\s*(" + INSERT_UPDATE + "|" + INSERT + "|" + UPDATE + "|" + REMOVE + ")?\\s+\\w.*";

		String findItemRegex="(insert|update|insert_update|remove){1} [a-zA-Z0-9$_]+";
		Pattern pattern=Pattern.compile(findItemRegex);

		Matcher matcher=pattern.matcher("insert SimpleBannerComponent;$contentCV[unique=true];uid[unique=true];name;&componentRef;urlLink");


		if (matcher.find())
		{
			System.out.println(matcher.group(0));
		}else{
			System.out.println("Nothing !!!");
		}


	}

}
