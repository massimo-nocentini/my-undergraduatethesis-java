package util;

import model.VertexStatsRecorderUnitTest;

import org.junit.Test;

import piping.PlainTextStatsPipeFilterUnitTest;

public class MassiveComputations {

	/**
	 * This method allow to execute several test method that treat a massive
	 * computation on a huge number of models. All of the following methods
	 * always pass, no assertions are fixed here.
	 */
	@Test
	public void massive() {

		vertex_stats_recorder_unit_tests_computations();

		PlainTextStatsPipeFilterUnitTest plainTextStatsPipeFilterUnitTest = new PlainTextStatsPipeFilterUnitTest();
		plainTextStatsPipeFilterUnitTest
				.generating_massive_stats_reports_forall_sbml_models_contained_in_standard_folder();

		plainTextStatsPipeFilterUnitTest
				.generating_massive_stats_reports_forall_sbml_models_collapsing_sources_contained_in_standard_folder();

	}

	private void vertex_stats_recorder_unit_tests_computations() {
		VertexStatsRecorderUnitTest vertexStatsRecorderUnitTest = new VertexStatsRecorderUnitTest();

		vertexStatsRecorderUnitTest
				.check_species_presence_in_various_sbml_models_contained_in_curated_folder();

		vertexStatsRecorderUnitTest
				.check_species_presence_in_various_sbml_models_contained_in_aae_folder();

		// vertexStatsRecorderUnitTest
		// .check_species_presence_in_a_huge_number_of_sbml_models_contained_in_kyoto_database();

		vertexStatsRecorderUnitTest
				.check_species_presence_in_standard_sbml_models();

		vertexStatsRecorderUnitTest
				.scan_all_standard_sbml_models_should_produce_consistent_informations();

	}

}
