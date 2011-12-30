package piping;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import model.OurModel;

import org.junit.Test;

import dotInterface.DotFileUtilHandler;
import dotInterface.DotFileUtilHandler.DotUtilAction;

public class ConnectedComponentsInfoPipeFilterUnitTest {

	public static final File serialized_data_structure_for_standard_models_file_handler = new File(
			DotFileUtilHandler
					.dotOutputFolderPathName()
					.concat("massive-connected-components-info-serialized-for-standard-models.datastructure"));

	@Test
	public void massive_tests_for_building_a_file_with_connected_components_infos_for_models_contained_in_standard_directory() {

		final PipeFilter tarjanPipeFilter = PipeFilterFactory
				.MakeTarjanPipeFilter();

		final ConnectedComponentsInfoPipeFilter connectedComponentsInfoPipeFilter = PipeFilterFactory
				.MakeConnectedComponentsInfoPipeFilter();

		connectedComponentsInfoPipeFilter.pipeAfter(tarjanPipeFilter);

		DotUtilAction<File> action = new DotUtilAction<File>() {

			@Override
			public void apply(File element) {

				String pipeline_name = "massive-connected-components-info-serialized-for-standard-models-"
						.concat(element.getName().substring(0,
								element.getName().lastIndexOf(".")));

				connectedComponentsInfoPipeFilter.applyWithListener(
						pipeline_name,
						OurModel.makeOurModelFrom(element.getAbsolutePath()),
						new PipeFilterComputationListenerNullObject());

			}
		};

		DotFileUtilHandler.mapOnFilesInFolderFilteringByExtension(
				DotFileUtilHandler.getSbmlExampleModelsFolder(),
				DotFileUtilHandler.getSBMLFileExtension(), action, false);

		try {

			connectedComponentsInfoPipeFilter
					.writeOn(new FileOutputStream(
							serialized_data_structure_for_standard_models_file_handler));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
