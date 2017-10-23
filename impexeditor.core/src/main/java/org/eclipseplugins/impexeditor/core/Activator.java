package org.eclipseplugins.impexeditor.core;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipseplugins.impexeditor.core.editor.ImpexColorConstants;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "impexeditor.core";

	// The shared instance
	private static Activator plugin;
	
	public boolean started;
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin  = this;
		started = true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		started = false;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	@Override
	protected void initializeImageRegistry(final ImageRegistry registry)
	{

		final Bundle bundle = Platform.getBundle(PLUGIN_ID);
		final IPath typPath = new Path("icons/type.gif");
		final URL urlType = FileLocator.find(bundle, typPath, null);
		final ImageDescriptor typeDesc = ImageDescriptor.createFromURL(urlType);
		registry.put(ImpexColorConstants.TYPE_IMAGE_ID, typeDesc);

		final IPath keywordPath = new Path("icons/keyword.png");
		final URL urlKeyword = FileLocator.find(bundle, keywordPath, null);
		final ImageDescriptor keywordDesc = ImageDescriptor.createFromURL(urlKeyword);
		registry.put(ImpexColorConstants.KEYWORD_IMAGE_ID, keywordDesc);
	}
}
