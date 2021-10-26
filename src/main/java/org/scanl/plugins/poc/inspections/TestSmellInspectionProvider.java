package org.scanl.plugins.poc.inspections;

import com.intellij.codeInspection.InspectionProfileEntry;
import com.intellij.codeInspection.InspectionToolProvider;
import com.intellij.codeInspection.LocalInspectionTool;
import org.jetbrains.annotations.NotNull;

public class TestSmellInspectionProvider implements InspectionToolProvider {
	/**
	 * Query method for inspection tools provided by a plugin.
	 *
	 * @return classes that extend {@link InspectionProfileEntry}
	 */
	@Override
	public Class<? extends LocalInspectionTool> @NotNull [] getInspectionClasses() {
		return new Class[]{
				RedundantPrintInspection.class,
				EmptyMethodInspection.class
		};
	}
}
