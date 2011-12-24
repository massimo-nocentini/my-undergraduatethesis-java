package model;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class ConnectedComponentInfoRecorder {

	private final ConnectedComponentInfoDataStructure dataStructure = new ConnectedComponentInfoDataStructure();

	public static class ConnectedComponentInfoDataStructure {

		private final SortedMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>> map;

		public ConnectedComponentInfoDataStructure() {
			this(
					new TreeMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>>());
		}

		public ConnectedComponentInfoDataStructure(
				SortedMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>> other) {

			this.map = other == null ? new TreeMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>>()
					: new TreeMap<String, SortedMap<String, SortedMap<Integer, SortedSet<String>>>>(
							other);
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
	}

	public void putTuple(String species, String componentType,
			Integer cardinality, String modelName) {

		this.dataStructure.putSpecies(species);

		this.dataStructure.putComponentType(species, componentType);

		this.dataStructure.putCardinality(species, componentType, cardinality);

		this.dataStructure.putModel(species, componentType, cardinality,
				modelName);

	}

	public boolean isDataStructureEquals(
			ConnectedComponentInfoDataStructure other) {

		return this.dataStructure.equals(other);
	}

	public void writeDataStructure(OutputStream outputStream) {
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(
					outputStream);

			objectOutputStream.writeObject(this.dataStructure.map);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
