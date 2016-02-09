package org.eclipseplugins.impexeditor.formatter;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

public class Application implements IApplication {

	@Override
	public Object start(IApplicationContext context) throws Exception {
		Object argsValue = context.getArguments().get("application.args");
		if (argsValue == null || !(argsValue instanceof String[])) {
			System.out.println("Usage: <Code> <Check Style File>");
		} else {
			String[] args = (String[]) argsValue;
			if (args.length != 2) {
				System.out
						.println("Require two arguments: <Code> <Check Style File>");
			} else {
				String codeFile = args[0];
				String eclipseCodeStyleFile = args[1];
				String formatted = EclipseCodeStyleFormatter.format(
				    FileUtils.readFileToString(new File(codeFile)),
				    new File(eclipseCodeStyleFile), 20);
				System.out.println(formatted);
			}
		}
		return IApplication.EXIT_OK;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}


}
