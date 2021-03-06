package model;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import util.IntegerCounter;

public class ConnectedComponentInfoRecorder {

	private final ConnectedComponentInfoDataStructure dataStructure = ConnectedComponentInfoDataStructure
			.make_empty_datastructure();

	private final SortedMap<String, SortedMap<String, Set<Vertex>>> tuples_by_models_typed = new TreeMap<String, SortedMap<String, Set<Vertex>>>();
	private final SortedMap<String, SortedMap<String, IntegerCounter>> components_by_models = new TreeMap<String, SortedMap<String, IntegerCounter>>();

	public void recordTupleByModel(String model_name, String vertex_type,
			Set<Vertex> members) {

		if (tuples_by_models_typed.containsKey(model_name) == false) {
			tuples_by_models_typed.put(model_name,
					new TreeMap<String, Set<Vertex>>());

			components_by_models.put(model_name,
					new TreeMap<String, IntegerCounter>());
		}

		SortedMap<String, Set<Vertex>> vertex_type_for_model = tuples_by_models_typed
				.get(model_name);

		if (vertex_type_for_model.containsKey(vertex_type) == false) {
			vertex_type_for_model.put(vertex_type, new TreeSet<Vertex>());

			components_by_models.get(model_name).put(vertex_type,
					new IntegerCounter());
		}

		components_by_models.get(model_name).get(vertex_type).increment();

		Set<Vertex> stored_set = vertex_type_for_model.get(vertex_type);
		for (Vertex possibly_new_vertex : members) {

			if (stored_set.contains(possibly_new_vertex) == false) {
				stored_set.add(possibly_new_vertex);
			}
		}

	}

	public void close_pool() {

		for (String model_name : tuples_by_models_typed.keySet()) {

			for (String vertex_type : tuples_by_models_typed.get(model_name)
					.keySet()) {

				this.dataStructure.record_tuples_by_model(model_name,
						vertex_type, tuples_by_models_typed.get(model_name)
								.get(vertex_type), components_by_models);

			}
		}
	}

	public static class ConnectedComponentInfoDataStructure implements
			Serializable {

		public static class ConnectedComponentPairCounter implements
				Serializable {

			/**
			 * 
			 */
			private static final long serialVersionUID = -714178501616999417L;
			private final IntegerCounter components_counter = new IntegerCounter();
			private final IntegerCounter vertices_counter = new IntegerCounter();
			private final Set<String> set_of_members = new HashSet<String>();
			private boolean members_already_added = false;

			public void increment_vertices(Set<Vertex> members) {
				vertices_counter.increment(members.size());

				if (members_already_added == true) {
					throw new RuntimeException(
							"Impossible to have more than one set of member due to uniqueness of models");
				}

				for (Vertex vertex : members) {
					this.set_of_members.add(vertex
							.buildVertexUniqueIdentifier());
				}

				members_already_added = true;

			}

			public void increment_components(int size) {
				components_counter.increment(size);
			}

			@Override
			public String toString() {
				return "(C: ".concat(components_counter.getCount().toString())
						.concat(", V: ")
						.concat(vertices_counter.getCount().toString())
						.concat(":")
						.concat(String.valueOf(set_of_members.size()))
						.concat(")");
			}

			@Override
			public int hashCode() {
				return this.toString().hashCode();
			}

			@Override
			public boolean equals(Object obj) {

				if (obj == null) {
					return false;
				}

				return toString().equals(obj.toString());
			}

			public ConnectedComponentPairCounter average(int models_count) {
				ConnectedComponentPairCounter averaged = new ConnectedComponentPairCounter();

				averaged.components_counter.increment(this.components_counter
						.getCount() / models_count);
				averaged.vertices_counter.increment(this.vertices_counter
						.getCount() / models_count);

				return averaged;
			}

			public ConnectedComponentPairCounter filter_members_respect(
					Set<String> members_to_intersect) {

				ConnectedComponentPairCounter result = this.average(1);

				for (String my_vertex : set_of_members) {

					if (members_to_intersect.contains(my_vertex) == true) {
						result.set_of_members.add(my_vertex);
					}
				}

				return result;
			}
		}

		/**
		 * 
		 */
		private static final long serialVersionUID = -9063923742355150277L;

		private final SortedMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>> tuples_by_species;
		private SortedMap<String, SortedMap<String, ConnectedComponentPairCounter>> tuples_by_models = new TreeMap<String, SortedMap<String, ConnectedComponentPairCounter>>();

		public static ConnectedComponentInfoDataStructure make_empty_datastructure() {
			return new ConnectedComponentInfoDataStructure(
					new TreeMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>>(),
					new TreeMap<String, SortedMap<String, ConnectedComponentPairCounter>>());
		}

		public static ConnectedComponentInfoDataStructure make_datastructure_with_species_map(
				SortedMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>> other_species_map) {

			return new ConnectedComponentInfoDataStructure(
					other_species_map,
					new TreeMap<String, SortedMap<String, ConnectedComponentPairCounter>>());
		}

		public static ConnectedComponentInfoDataStructure make_datastructure_with_models_map(
				SortedMap<String, SortedMap<String, ConnectedComponentPairCounter>> other_models_map) {

			return new ConnectedComponentInfoDataStructure(
					new TreeMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>>(),
					other_models_map);
		}

		public static ConnectedComponentInfoDataStructure make_datastructure_with_both_maps(
				SortedMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>> other_species_map,
				SortedMap<String, SortedMap<String, ConnectedComponentPairCounter>> other_models_map) {

			return new ConnectedComponentInfoDataStructure(other_species_map,
					other_models_map);
		}

		private ConnectedComponentInfoDataStructure(
				SortedMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>> other_species_map,
				SortedMap<String, SortedMap<String, ConnectedComponentPairCounter>> other_models_map) {

			this.tuples_by_species = other_species_map;

			this.tuples_by_models = other_models_map;
		}

		public ConnectedComponentInfoDataStructure(InputStream inputStream) {

			ConnectedComponentInfoDataStructure deserialized = null;
			try {
				ObjectInputStream objectInputStream = new ObjectInputStream(
						inputStream);

				deserialized = (ConnectedComponentInfoDataStructure) objectInputStream
						.readObject();

				objectInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			if (deserialized != null) {

				this.tuples_by_species = deserialized.tuples_by_species;
				this.tuples_by_models = deserialized.tuples_by_models;

			} else {
				this.tuples_by_species = null;
				this.tuples_by_models = null;
			}

		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime
					* result
					+ ((tuples_by_species == null) ? 0 : tuples_by_species
							.hashCode());
			result = prime
					* result
					+ ((tuples_by_models == null) ? 0 : tuples_by_models
							.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof ConnectedComponentInfoDataStructure)) {
				return false;
			}
			ConnectedComponentInfoDataStructure other = (ConnectedComponentInfoDataStructure) obj;
			if (tuples_by_species == null) {
				if (other.tuples_by_species != null) {
					return false;
				}
			} else if (!tuples_by_species.equals(other.tuples_by_species)) {
				return false;
			}

			if (tuples_by_models == null) {
				if (other.tuples_by_models != null) {
					return false;
				}
			} else if (!tuples_by_models.equals(other.tuples_by_models)) {
				return false;
			}
			return true;
		}

		private void record_tuples_by_species(String species,
				String component_type, Integer cardinality, String model_name) {

			if (this.tuples_by_species.containsKey(species) == false) {

				this.tuples_by_species
						.put(species,
								new TreeMap<String, SortedMap<Integer, SortedSet<String>>>());
			}

			SortedMap<String, SortedMap<Integer, SortedSet<String>>> mapBySpecies = this.tuples_by_species
					.get(species);

			if (mapBySpecies.containsKey(component_type) == false) {

				mapBySpecies.put(component_type,
						new TreeMap<Integer, SortedSet<String>>());
			}

			SortedMap<Integer, SortedSet<String>> mapBySpeciesAndComponent = this.tuples_by_species
					.get(species).get(component_type);

			if (mapBySpeciesAndComponent.containsKey(cardinality) == false) {
				mapBySpeciesAndComponent
						.put(cardinality, new TreeSet<String>());
			}

			this.tuples_by_species.get(species).get(component_type)
					.get(cardinality).add(model_name);

		}

		private void record_tuples_by_model(
				String model_name,
				String vertex_type,
				Set<Vertex> members,
				SortedMap<String, SortedMap<String, IntegerCounter>> components_by_models) {

			if (tuples_by_models.containsKey(model_name) == false) {
				tuples_by_models.put(model_name,
						new TreeMap<String, ConnectedComponentPairCounter>());
			}

			SortedMap<String, ConnectedComponentPairCounter> vertex_type_for_model = tuples_by_models
					.get(model_name);

			if (vertex_type_for_model.containsKey(vertex_type) == false) {

				vertex_type_for_model.put(vertex_type,
						new ConnectedComponentPairCounter());
			}

			vertex_type_for_model.get(vertex_type).increment_vertices(members);

			vertex_type_for_model.get(vertex_type).increment_components(
					components_by_models.get(model_name).get(vertex_type)
							.getCount());
		}

		public Object[][] build_rows_data() {

			Object[] build_columns_data = this.build_columns_data();
			int column_dimension = build_columns_data.length;

			int models_count = tuples_by_models.size();
			Object[][] rows_data = new Object[models_count + 1][column_dimension];
			int row_index = 0;

			Set<String> sources_collecting_parameter = new TreeSet<String>();
			Set<String> sinks_collecting_parameter = new TreeSet<String>();
			Set<String> whites_collecting_parameter = new TreeSet<String>();

			this.fill_in_collecting_parameter_with_sources_and_sinks_species(
					sources_collecting_parameter, sinks_collecting_parameter,
					whites_collecting_parameter);

			for (String model : tuples_by_models.keySet()) {

				Object[] row_data = new Object[column_dimension];
				for (String vertex_type : tuples_by_models.get(model).keySet()) {

					ConnectedComponentPairCounter pairCounter = tuples_by_models
							.get(model).get(vertex_type);

					if (vertex_type.equals(VertexType.Sources.toString())) {

						pairCounter = pairCounter
								.filter_members_respect(sources_collecting_parameter);

					} else if (vertex_type.equals(VertexType.Sinks.toString())) {

						pairCounter = pairCounter
								.filter_members_respect(sinks_collecting_parameter);

					} else if (vertex_type.equals(VertexType.Whites.toString())) {

						pairCounter = pairCounter
								.filter_members_respect(whites_collecting_parameter);
					} else {
						throw new RuntimeException(
								"Impossible to recognize the verte type: "
										+ vertex_type);
					}

					row_data[order_vertex_type(vertex_type)] = pairCounter;

				}

				row_data[0] = model;

				rows_data[row_index] = row_data;

				row_index = row_index + 1;
			}

			Object[] average_row_data = new Object[column_dimension];

			// we start from 1 in order to skip the model column
			for (int column_index = 1; column_index < column_dimension; column_index = column_index + 1) {

				VertexType column = (VertexType) build_columns_data[column_index];

				ConnectedComponentPairCounter pair_counter_for_average = new ConnectedComponentPairCounter();

				for (String model : tuples_by_models.keySet()) {

					ConnectedComponentPairCounter pairCounter = tuples_by_models
							.get(model).get(column.toString());

					pair_counter_for_average.components_counter
							.increment(pairCounter.components_counter
									.getCount());

					pair_counter_for_average.vertices_counter
							.increment(pairCounter.vertices_counter.getCount());
				}

				average_row_data[order_vertex_type(column.toString())] = pair_counter_for_average
						.average(models_count);
			}

			average_row_data[0] = "Average pairs";

			rows_data[rows_data.length - 1] = average_row_data;

			return rows_data;
		}

		public int order_vertex_type(String vertex_type) {

			int order = 3;

			VertexType typed_vertex_type = VertexType.valueOf(vertex_type);

			if (typed_vertex_type.equals(VertexType.Sources)) {
				order = 1;
			} else if (typed_vertex_type.equals(VertexType.Whites)) {
				order = 2;
			}

			return order;
		}

		public static void put_tuples_by_species_into(
				SortedMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>> map,
				String species, String component_type, Integer cardinality,
				String model_name) {

			// here we use an object of this class type in order to modify only
			// the map using the behavior already written.
			ConnectedComponentInfoDataStructure dataStructure = ConnectedComponentInfoDataStructure
					.make_datastructure_with_species_map(map);

			dataStructure.record_tuples_by_species(species, component_type,
					cardinality, model_name);

		}

		public static void put_tuples_by_models_into(
				SortedMap<String, SortedMap<String, ConnectedComponentPairCounter>> map,
				String model_name, String vertex_type, Set<Vertex> members,
				SortedMap<String, SortedMap<String, IntegerCounter>> components) {

			// here we use an object of this class type in order to modify only
			// the map using the behavior already written.
			ConnectedComponentInfoDataStructure dataStructure = ConnectedComponentInfoDataStructure
					.make_datastructure_with_models_map(map);

			dataStructure.record_tuples_by_model(model_name, vertex_type,
					members, components);

		}

		public void serializeYourselfOn(OutputStream outputStream) {
			try {
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(
						outputStream);

				objectOutputStream.writeObject(this);

				objectOutputStream.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void fill_datas_into(
				SortedMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>> map_to_fill_in) {

			map_to_fill_in.putAll(this.tuples_by_species);
		}

		public Object[] build_columns_data() {

			Object[] columnNames = {
					"Models".concat(" (")
							.concat(String.valueOf(tuples_by_models.size()))
							.concat(")"), VertexType.Sources,
					VertexType.Whites, VertexType.Sinks };

			return columnNames;
		}

		public int compute_number_of_components_among_all_models() {

			IntegerCounter numer_of_components = new IntegerCounter();

			for (String model : tuples_by_models.keySet()) {
				for (String component_type : tuples_by_models.get(model)
						.keySet()) {
					numer_of_components.increment(tuples_by_models.get(model)
							.get(component_type).components_counter.getCount());
				}
			}

			return numer_of_components.getCount();
		}

		public Collection<GlobalStatisticalGroupedInfoByType> build_statistical_info_grouping_by_component_type_combination() {

			return build_statistical_info_grouping_by_component_type_combination(new TreeSet<String>());
		}

		public Collection<GlobalStatisticalGroupedInfoByType> build_statistical_info_grouping_by_component_type_combination(
				Set<String> requested_models) {

			Map<Set<String>, IntegerCounter> working_map = new LinkedHashMap<Set<String>, IntegerCounter>();

			Set<String> sources_only = new HashSet<String>();
			sources_only.add(VertexType.Sources.toString());

			Set<String> sinks_only = new HashSet<String>();
			sinks_only.add(VertexType.Sinks.toString());

			Set<String> whites_only = new HashSet<String>();
			whites_only.add(VertexType.Whites.toString());

			Set<String> sources_and_sinks = new HashSet<String>();
			sources_and_sinks.add(VertexType.Sources.toString());
			sources_and_sinks.add(VertexType.Sinks.toString());

			Set<String> sources_and_whites = new HashSet<String>();
			sources_and_whites.add(VertexType.Sources.toString());
			sources_and_whites.add(VertexType.Whites.toString());

			Set<String> sinks_and_whites = new HashSet<String>();
			sinks_and_whites.add(VertexType.Sinks.toString());
			sinks_and_whites.add(VertexType.Whites.toString());

			Set<String> all_three_vertex_types = new HashSet<String>();
			all_three_vertex_types.add(VertexType.Sources.toString());
			all_three_vertex_types.add(VertexType.Sinks.toString());
			all_three_vertex_types.add(VertexType.Whites.toString());

			working_map.put(sources_only, new IntegerCounter());
			working_map.put(sinks_only, new IntegerCounter());
			working_map.put(whites_only, new IntegerCounter());
			working_map.put(sources_and_sinks, new IntegerCounter());
			working_map.put(sources_and_whites, new IntegerCounter());
			working_map.put(sinks_and_whites, new IntegerCounter());
			working_map.put(all_three_vertex_types, new IntegerCounter());

			Set<String> working_collector = new HashSet<String>();
			int analyzed_species = 0;
			for (String species : this.tuples_by_species.keySet()) {

				working_collector.clear();

				SortedMap<String, SortedMap<Integer, SortedSet<String>>> component_types_by_species = tuples_by_species
						.get(species);

				boolean include_species_in_result = false;
				for (String component_type : component_types_by_species
						.keySet()) {

					boolean have_this_component_type_the_requested_models = false;
					cardinality_loop: for (Integer cardinality : component_types_by_species
							.get(component_type).keySet()) {

						for (String model : component_types_by_species.get(
								component_type).get(cardinality)) {

							if (requested_models.size() == 0
									|| requested_models.contains(model)) {

								have_this_component_type_the_requested_models = true;
								break cardinality_loop;
							}
						}
					}

					if (have_this_component_type_the_requested_models == true) {

						include_species_in_result = true;
						// here we call the valueOf method to be sure
						// that the input
						// String is a valid VertexType value
						VertexType vertexType = VertexType
								.valueOf(component_type);

						working_collector.add(vertexType.toString());

					}
				}

				if (include_species_in_result == true) {

					analyzed_species = analyzed_species + 1;

					// now that we have finished to analize all the component
					// types
					// for the current species, we can increase the counter
					working_map.get(working_collector).increment();
				}
			}

			Collection<GlobalStatisticalGroupedInfoByType> result = new LinkedList<GlobalStatisticalGroupedInfoByType>();

			// result.add("(analyzed_species: ".concat(String.valueOf(
			// analyzed_species).concat(")")));

			for (Entry<Set<String>, IntegerCounter> entry : working_map
					.entrySet()) {

				GlobalStatisticalGroupedInfoByType info_object = new GlobalStatisticalGroupedInfoByType(
						entry.getKey(), entry.getValue(), analyzed_species);

				result.add(info_object);

			}

			return result;

		}

		public void fill_in_collecting_parameter_with_sources_and_sinks_species(
				Set<String> sources_collecting_parameter,
				Set<String> sinks_collecting_parameter,
				Set<String> whites_collecting_parameter) {

			sources_collecting_parameter.clear();
			sinks_collecting_parameter.clear();

			Map<Set<String>, Set<String>> working_map = new HashMap<Set<String>, Set<String>>();

			Set<String> sources_only = new HashSet<String>();
			sources_only.add(VertexType.Sources.toString());

			Set<String> sinks_only = new HashSet<String>();
			sinks_only.add(VertexType.Sinks.toString());

			Set<String> whites_only = new HashSet<String>();
			whites_only.add(VertexType.Whites.toString());

			working_map.put(sources_only, new HashSet<String>());
			working_map.put(sinks_only, new HashSet<String>());
			working_map.put(whites_only, new HashSet<String>());

			Set<String> working_collector = new HashSet<String>();

			for (String species : this.tuples_by_species.keySet()) {

				working_collector.clear();

				SortedMap<String, SortedMap<Integer, SortedSet<String>>> component_types_by_species = tuples_by_species
						.get(species);

				for (String component_type : component_types_by_species
						.keySet()) {

					// here we call the valueOf method to be sure
					// that the input
					// String is a valid VertexType value
					VertexType vertexType = VertexType.valueOf(component_type);

					working_collector.add(vertexType.toString());
				}

				// with this condition we are sure to consider only the species
				// which are sources or sinks due the presence of only this two
				// set in the working map
				if (working_map.containsKey(working_collector)) {

					working_map.get(working_collector).add(species);
				}
			}

			sources_collecting_parameter.addAll(working_map.get(sources_only));
			sinks_collecting_parameter.addAll(working_map.get(sinks_only));
			whites_collecting_parameter.addAll(working_map.get(whites_only));
		}

		public static class GlobalStatisticalGroupedInfoByType {

			private final int analyzed_species;
			private final Set<String> key;
			private final IntegerCounter value;

			public GlobalStatisticalGroupedInfoByType(Set<String> key,
					IntegerCounter value, int analyzed_species) {

				this.key = key;
				this.value = value;

				this.analyzed_species = analyzed_species;
			}

			@Override
			public String toString() {
				Integer count = value.getCount();

				DecimalFormat formatter = new DecimalFormat("#.###");

				return "(types: "
						.concat(key.toString())
						.concat(", count: ")
						.concat(count.toString())
						.concat(", distribution: ")
						.concat(formatter.format(
								(count / (double) analyzed_species) * 100)
								.concat("%")).concat(")");

			}

			public boolean isKeyEquals(Set<String> other) {

				return this.key.equals(other);
			}

		}
	}

	public void recordTupleBySpecies(String species, String componentType,
			Integer cardinality, String modelName) {

		ConnectedComponentInfoDataStructure.put_tuples_by_species_into(
				this.dataStructure.tuples_by_species, species, componentType,
				cardinality, modelName);

	}

	public boolean isDataStructureEquals(
			ConnectedComponentInfoDataStructure other) {

		return this.dataStructure.equals(other);
	}

	public void toJavaSerialization(OutputStream outputStream) {

		this.dataStructure.serializeYourselfOn(outputStream);
	}

}
