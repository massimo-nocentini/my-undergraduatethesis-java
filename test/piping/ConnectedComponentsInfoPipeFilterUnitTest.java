package piping;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import model.OurModel;

import org.junit.Test;

import dotInterface.DotFileUtilHandler;
import dotInterface.DotFileUtilHandler.DotUtilAction;

public class ConnectedComponentsInfoPipeFilterUnitTest {

	/**
	 * This is the file handler that reference a destination for the standard
	 * models data structure.
	 */
	public static final File serialized_data_structure_for_standard_models_file_handler = new File(
			DotFileUtilHandler
					.dotOutputFolderPathName()
					.concat("massive-connected-components-info-serialized-for-standard-models.datastructure"));

	/**
	 * This is the file handler that reference a destination for the BioCyc
	 * models data structure.
	 */
	public static final File serialized_data_structure_for_BioCyc_models_file_handler = new File(
			DotFileUtilHandler
					.dotOutputFolderPathName()
					.concat("massive-connected-components-info-serialized-for-BioCyc-models.datastructure"));

	@Test
	public void massive_tests_for_building_a_file_with_connected_components_infos_for_models_contained_in_standard_directory() {

		apply_recording_on(DotFileUtilHandler.getSbmlExampleModelsFolder(),
				serialized_data_structure_for_standard_models_file_handler,
				false, "standard-models");
	}

	public static void apply_recording_on(String scanning_folder,
			File output_file, boolean recursive_scanning,
			final String pipeline_prefix) {

		final PipeFilter tarjanPipeFilter = PipeFilterFactory
				.MakeTarjanPipeFilter();

		final ConnectedComponentsInfoPipeFilter connectedComponentsInfoPipeFilter = PipeFilterFactory
				.MakeConnectedComponentsInfoPipeFilter();

		connectedComponentsInfoPipeFilter.pipeAfter(tarjanPipeFilter);

		DotUtilAction<File> action = new DotUtilAction<File>() {

			@Override
			public void apply(File element) {

				String pipeline_name = "massive-connected-components-info-serialized-for-"
						.concat(pipeline_prefix)
						.concat("-")
						.concat(element.getName().substring(0,
								element.getName().lastIndexOf(".")));

				connectedComponentsInfoPipeFilter.apply(pipeline_name,
						OurModel.makeOurModelFrom(element.getAbsolutePath()));

			}
		};

		DotFileUtilHandler.mapOnFilesInFolderFilteringByExtension(
				scanning_folder, DotFileUtilHandler.getSBMLFileExtension(),
				action, recursive_scanning);

		try {

			connectedComponentsInfoPipeFilter.writeOn(new FileOutputStream(
					output_file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
