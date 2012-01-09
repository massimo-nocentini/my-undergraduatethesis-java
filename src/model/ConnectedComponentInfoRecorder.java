package model;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import util.IntegerCounter;

public class ConnectedComponentInfoRecorder {

	private final ConnectedComponentInfoDataStructure dataStructure = ConnectedComponentInfoDataStructure
			.make_empty_datastructure();

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

			public void increment_by(int size) {
				components_counter.increment();
				vertices_counter.increment(size);
			}

			@Override
			public String toString() {
				return "(C: ".concat(components_counter.getCount().toString())
						.concat(", V: ")
						.concat(vertices_counter.getCount().toString())
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

			this.tuples_by_species = deserialized != null ? deserialized.tuples_by_species
					: null;

			this.tuples_by_models = deserialized != null ? deserialized.tuples_by_models
					: null;
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

		public void record_tuples_by_species(String species,
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

		public void record_tuples_by_model(String model_name,
				String vertex_type, int members_count) {

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

			vertex_type_for_model.get(vertex_type).increment_by(members_count);
		}

		public Object[][] build_rows_data() {

			int column_dimension = this.build_columns_data().length;

			Object[][] rows_data = new Object[tuples_by_models.size()][column_dimension];
			int row_index = 0;

			for (String model : tuples_by_models.keySet()) {

				Object[] row_data = new Object[column_dimension];
				for (String vertex_type : tuples_by_models.get(model).keySet()) {

					row_data[order_vertex_type(vertex_type)] = tuples_by_models
							.get(model).get(vertex_type);
				}

				row_data[0] = model;

				rows_data[row_index] = row_data;

				row_index = row_index + 1;
			}

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
				String model_name, String vertex_type, int members_count) {

			// here we use an object of this class type in order to modify only
			// the map using the behavior already written.
			ConnectedComponentInfoDataStructure dataStructure = ConnectedComponentInfoDataStructure
					.make_datastructure_with_models_map(map);

			dataStructure.record_tuples_by_model(model_name, vertex_type,
					members_count);

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

	public void recordTupleByModel(String model_name, String vertex_type,
			int members_count) {

		ConnectedComponentInfoDataStructure.put_tuples_by_models_into(
				this.dataStructure.tuples_by_models, model_name, vertex_type,
				members_count);
	}

}
