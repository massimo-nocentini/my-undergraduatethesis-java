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

	private final ConnectedComponentInfoDataStructure dataStructure = new ConnectedComponentInfoDataStructure();

	private final SortedMap<String, SortedMap<String, ConnectedComponentPairCounter>> models_map = new TreeMap<String, SortedMap<String, ConnectedComponentPairCounter>>();

	public static class ConnectedComponentPairCounter {

		private final IntegerCounter components_counter = new IntegerCounter();
		private final IntegerCounter vertices_counter = new IntegerCounter();

		public void increment_by(int size) {
			components_counter.increment();
			vertices_counter.increment(size);
		}

		@Override
		public String toString() {
			return "(C: ".concat(components_counter.getCount().toString())
					.concat(", V:")
					.concat(vertices_counter.getCount().toString()).concat(")");
		}

	}

	public static class ConnectedComponentInfoDataStructure implements
			Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -9063923742355150277L;

		private final SortedMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>> map;

		public ConnectedComponentInfoDataStructure() {
			this(
					new TreeMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>>());
		}

		public ConnectedComponentInfoDataStructure(
				SortedMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>> other) {

			this.map = other == null ? new TreeMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>>()
					: other;
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

			this.map = deserialized != null ? deserialized.map : null;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((map == null) ? 0 : map.hashCode());
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
			if (map == null) {
				if (other.map != null) {
					return false;
				}
			} else if (!map.equals(other.map)) {
				return false;
			}
			return true;
		}

		public void putSpecies(String species) {

			if (this.map.containsKey(species) == false) {

				this.map.put(
						species,
						new TreeMap<String, SortedMap<Integer, SortedSet<String>>>());
			}
		}

		public void putComponentType(String species, String componentType) {

			SortedMap<String, SortedMap<Integer, SortedSet<String>>> mapBySpecies = this.map
					.get(species);

			if (mapBySpecies.containsKey(componentType) == false) {

				mapBySpecies.put(componentType,
						new TreeMap<Integer, SortedSet<String>>());
			}

		}

		public void putCardinality(String species, String componentType,
				Integer cardinality) {

			SortedMap<Integer, SortedSet<String>> mapBySpeciesAndComponent = this.map
					.get(species).get(componentType);

			if (mapBySpeciesAndComponent.containsKey(cardinality) == false) {
				mapBySpeciesAndComponent
						.put(cardinality, new TreeSet<String>());
			}

		}

		public void putModel(String species, String componentType,
				Integer cardinality, String modelName) {

			this.map.get(species).get(componentType).get(cardinality)
					.add(modelName);
		}

		public static void putIntoMap(
				SortedMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>> map,
				String species, String component_type, Integer cardinality,
				String model_name) {

			// here we use an object of this class type in order to modify only
			// the map using the behavior already written.
			ConnectedComponentInfoDataStructure dataStructure = new ConnectedComponentInfoDataStructure(
					map);

			dataStructure.putSpecies(species);

			dataStructure.putComponentType(species, component_type);

			dataStructure.putCardinality(species, component_type, cardinality);

			dataStructure.putModel(species, component_type, cardinality,
					model_name);

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

			map_to_fill_in.putAll(this.map);
		}
	}

	public void recordTupleBySpecies(String species, String componentType,
			Integer cardinality, String modelName) {

		ConnectedComponentInfoDataStructure.putIntoMap(this.dataStructure.map,
				species, componentType, cardinality, modelName);

	}

	public boolean isDataStructureEquals(
			ConnectedComponentInfoDataStructure other) {

		return this.dataStructure.equals(other);
	}

	public void toJavaSerialization(OutputStream outputStream) {

		this.dataStructure.serializeYourselfOn(outputStream);
	}

	public void recordTupleByModel(String modelName, String vertex_type,
			int members_count) {

		if (this.models_map.containsKey(modelName) == false) {
			this.models_map.put(modelName,
					new TreeMap<String, ConnectedComponentPairCounter>());
		}

		SortedMap<String, ConnectedComponentPairCounter> vertex_type_for_model = this.models_map
				.get(modelName);

		if (vertex_type_for_model.containsKey(vertex_type) == false) {
			vertex_type_for_model.put(vertex_type,
					new ConnectedComponentPairCounter());
		}

		vertex_type_for_model.get(vertex_type).increment_by(members_count);
	}

}
