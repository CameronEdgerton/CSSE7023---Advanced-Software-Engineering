package bms.building;

import bms.exceptions.*;
import bms.floor.Floor;
import bms.hazardevaluation.RuleBasedHazardEvaluator;
import bms.hazardevaluation.WeightingBasedHazardEvaluator;
import bms.room.Room;
import bms.room.RoomType;
import bms.sensors.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages the initialisation and saving of buildings by reading and writing
 * data to a file.
 */
public class BuildingInitialiser {

    /**
     * Loads a list of buildings from a save file with the given filename.
     * @param filename path of the file from which to load a list of buildings
     * @return a list containing all the buildings loaded from the file
     * @throws IOException if an IOException is encountered when calling any
     *                     IO methods
     * @throws FileFormatException if the file format of the given file is
     *                             invalid
     */
    public static List<Building> loadBuildings(String filename)
            throws IOException, FileFormatException {
        List<Building> buildingList = new ArrayList<>();
        String buildingName;
        String line;
        int numFloors;
        Building building;

        try {
            // Read file for empty lines and throw error if empty line detected.
            BufferedReader emptyReader =
                    new BufferedReader(new FileReader(filename));
            while ((line = emptyReader.readLine()) != null) {
                if (line.length() == 0) {
                    throw new FileFormatException();
                }
            }
            // Read file for buildings
            BufferedReader reader =
                    new BufferedReader(new FileReader(filename));
            while ((buildingName = reader.readLine()) != null) {
                try {
                    numFloors = Integer.parseInt(reader.readLine());
                    if (numFloors < 0) {
                        throw new FileFormatException();
                    }
                } catch (NumberFormatException e) {
                    throw new FileFormatException();
                }
                try {
                    building = new Building(buildingName);
                } catch (IllegalArgumentException e) {
                    throw new FileFormatException();
                }
                // Add floors to building
                for (int i = 0; i < numFloors; ++i) {
                    try {
                        building.addFloor(readFloor(reader));
                    } catch (IllegalArgumentException | DuplicateFloorException
                            | NoFloorBelowException |
                            FloorTooSmallException e) {
                        throw new FileFormatException();
                    } catch (IOException e) {
                        throw new IOException();
                    }
                }
                buildingList.add(building);
            } reader.close();
        } catch (IOException e){
            throw new IOException();
        }
        return buildingList;
    }

    /*
    Checks if colon-delimited format has been violated
     */
    private static boolean badColons(String s, Integer int1, Integer int2) {
        long count = s.chars().filter(ch -> ch == ':').count();
        return count < int1 || count > int2;
    }

    /*
    Reads the file for a floor and returns it
     */
    private static Floor readFloor(BufferedReader reader)
            throws IOException, FileFormatException {
        String line = reader.readLine();
        String[] lineParts = line.split(":");
        int floorNumber;
        double width;
        double length;
        int numRooms;
        Floor floor;

        // Check if colon-delimited format violated.
        if (badColons(line, 3, 4)) {
            throw new FileFormatException();
        }
        try {
            floorNumber = Integer.parseInt(lineParts[0]);
            width = Double.parseDouble(lineParts[1]);
            length = Double.parseDouble(lineParts[2]);
            numRooms = Integer.parseInt(lineParts[3]);
        } catch (NumberFormatException e) {
            throw new FileFormatException();
        }
        try {
            floor = new Floor(floorNumber, width, length);
        } catch (IllegalArgumentException e) {
            throw new FileFormatException();
        }
        if (numRooms < 0) {
            throw new FileFormatException();
        }
        // Add rooms to floor
        for (int i = 0; i < numRooms; ++i) {
            try {
                floor.addRoom(roomReader(reader));
            } catch (DuplicateRoomException |
                    InsufficientSpaceException |
                    IllegalArgumentException e) {
                throw new FileFormatException();
            } catch (IOException e) {
                throw new IOException();
            }
        }
        // Handle empty / null maintenance order
        if (line.endsWith(":")) {
            throw new FileFormatException();
        }
        // Setup maintenance schedule
        if (lineParts.length == 5) {
            try {
                // Create room order list while parsing to integer
                List<Integer> maintenanceOrder =
                        Arrays.stream(lineParts[4].split(","))
                                .map(Integer::parseInt)
                                .collect(Collectors.toList());
                List<Room> roomOrder = new ArrayList<>();
                List<Integer> roomNums = new ArrayList<>();
                // Create list of room numbers and check if all are in
                // maintenanceOrder list.
                for (Room room : floor.getRooms()) {
                    roomNums.add(room.getRoomNumber());
                }
                for (int maintNum : maintenanceOrder) {
                    if (!roomNums.contains(maintNum)) {
                        throw new FileFormatException();
                    }
                }
                // Add rooms to roomOrder
                for (int roomNum : maintenanceOrder) {
                    if (floor.getRoomByNumber(roomNum) != null) {
                        roomOrder.add(floor.getRoomByNumber(roomNum));
                    }
                }
                try {
                    floor.createMaintenanceSchedule(roomOrder);
                } catch (IllegalArgumentException e) {
                    throw new FileFormatException();
                }
            } catch (NumberFormatException e) {
                throw new FileFormatException();
            }
        }
        return floor;
    }

    /*
    Reads the file for a room and returns it
     */
    private static Room roomReader(BufferedReader reader)
            throws IOException, FileFormatException {
        String line = reader.readLine();
        String[] lineParts = line.split(":");
        int roomNumber;
        double area;
        int numSensors;
        RoomType roomType;
        Room room;
        List<Sensor> sensors = new ArrayList<>();
        List<Integer> weightings = new ArrayList<>();

        // Check if colon-delimited format violated
        if (badColons(line, 3, 4)) {
            throw new FileFormatException();
        }
        try {
            roomNumber = Integer.parseInt(lineParts[0]);
            area = Double.parseDouble(lineParts[2]);
            numSensors = Integer.parseInt(lineParts[3]);
        } catch (NumberFormatException e) {
            throw new FileFormatException();
        }
        if (numSensors < 0) {
            throw new FileFormatException();
        }
        try {
            roomType = setRoomType(lineParts[1]);
        } catch (FileFormatException e) {
            throw new FileFormatException();
        }
        try {
            room = new Room(roomNumber, roomType, area);
        } catch (IllegalArgumentException e) {
            throw new FileFormatException();
        }
        // Separate sensors and their weightings and add to relevant lists
        for (int i = 0; i < numSensors; ++i) {
            try {
                Map<Sensor,Integer> sensorWeightMap = sensorReader(reader);
                for (Map.Entry<Sensor, Integer> entry :
                        sensorWeightMap.entrySet()) {
                    Sensor sensor = entry.getKey();
                    int weighting = entry.getValue();
                    sensors.add(sensor);
                    weightings.add(weighting);
                }
            } catch (IOException e) {
                throw new IOException();
            }
        }
        // Add sensors to room
        for (Sensor sensor : sensors) {
            try {
                room.addSensor(sensor);
            } catch (DuplicateSensorException e) {
                throw new FileFormatException();
            }
        }
        // Handle null hazardEvaluator
        if (line.endsWith(":")) {
            throw new FileFormatException();
        }
        // Setup hazard evaluator
        if (lineParts.length == 5) {
            List<HazardSensor> hazardSensors = setHazardList(sensors);
            if (lineParts[4].equals("RuleBased")) {
                room.setHazardEvaluator(new
                        RuleBasedHazardEvaluator(hazardSensors));
            } else if (lineParts[4].equals("WeightingBased")) {
                Map<HazardSensor,Integer> weightMap =
                        setWBHazardEvalMap(hazardSensors, sensors, weightings);
                try {
                    room.setHazardEvaluator(new
                            WeightingBasedHazardEvaluator(weightMap));
                } catch (IllegalArgumentException e) {
                    throw new FileFormatException();
                }
            } else {
                throw new FileFormatException();
            }
        }
        return room;
    }

    /*
    Returns the room type from a given string
     */
    private static RoomType setRoomType(String roomtype) throws
            FileFormatException {
        if (roomtype.equals("STUDY")) {
            return RoomType.STUDY;
        } else if (roomtype.equals("LABORATORY")) {
            return RoomType.LABORATORY;
        } else if (roomtype.equals("OFFICE")) {
            return RoomType.OFFICE;
        } else {
            throw new FileFormatException();
        }
    }

    /*
    Returns a map to create a Weighting Based Hazard Evaluator
     */
    private static Map<HazardSensor,Integer> setWBHazardEvalMap(
            List<HazardSensor> hazardList, List<Sensor> sensorList,
            List<Integer> weightingsList) {
        Map<HazardSensor,Integer> weightMap =
                new LinkedHashMap<>();
        for (int i = 0; i < sensorList.size(); i++) {
            weightMap.put(hazardList.get(i), weightingsList.get(i));
        }
        // Remove all sensors that were assigned the identifier weighting of -1
        // (because they do not actually have a weighting).
        weightMap.values().removeAll(Collections.singleton(-1));
        return weightMap;
    }

    /*
    Converts a list of sensors to a list of hazard sensors
     */
    private static List<HazardSensor> setHazardList(List<Sensor> sensors) {
        List<HazardSensor> hazardSensors = new ArrayList<>();
        for (Sensor sensor : sensors) {
            hazardSensors.add((HazardSensor) sensor);
        }
        return hazardSensors;
    }

    /*
    Return a map of sensors and their corresponding weighting.

    If a sensor does not have a weighting it is assigned an identifier
    weighting of -1.
    This allows for easy identification of sensors which should not be added
    to a weighting based hazard evaluator later.
     */
    private static Map<Sensor, Integer> sensorReader(BufferedReader reader)
            throws IOException, FileFormatException {
        String line = reader.readLine();
        String[] lineParts = line.split(":");
        int[] sensorReadings;
        Sensor sensor;
        int weighting;
        Map<Sensor, Integer> sensorWeightMap = new LinkedHashMap<>();
        String type = lineParts[0];

        try {
            sensorReadings = getSensorReadings(lineParts[1]);
            weighting = getWeighting(lineParts[1]);
        } catch (NumberFormatException e) {
            throw new FileFormatException();
        }
        // Handle null weightings
        if (line.endsWith("@")) {
            throw new FileFormatException();
        }
        if (type.equals("CarbonDioxideSensor")) {
            // Check if colon-delimited format violated
            if (badColons(line, 4, 4)) {
                throw new FileFormatException();
            }
            try {
                weighting = getWeighting(lineParts[4]);
                sensor = createCO2Sensor(lineParts, sensorReadings);
            } catch (IllegalArgumentException e) {
                throw new FileFormatException();
            }
        } else if (type.equals("NoiseSensor")) {
            // Check if colon-delimited format violated
            if (badColons(line, 2, 2)) {
                throw new FileFormatException();
            }
            try {
                weighting = getWeighting(lineParts[2]);
                sensor = createNoiseSensor(lineParts, sensorReadings);
            } catch (IllegalArgumentException e) {
                throw new FileFormatException();
            }
        } else if (type.equals("OccupancySensor")) {
            // Check if colon-delimited format violated
            if (badColons(line, 3, 3)) {
                throw new FileFormatException();
            }
            try {
                weighting = getWeighting(lineParts[3]);
                sensor = createOccupancySensor(lineParts, sensorReadings);
            } catch (IllegalArgumentException e) {
                throw new FileFormatException();
            }
        } else if (type.equals("TemperatureSensor")) {
            // Check if colon-delimited format violated
            if (badColons(line, 1, 1)) {
                throw new FileFormatException();
            }
            try {
                sensor = new TemperatureSensor(sensorReadings);
            } catch (IllegalArgumentException e) {
                throw new FileFormatException();
            }
        } else {
            throw new FileFormatException();
        }
        sensorWeightMap.put(sensor,weighting);
        return sensorWeightMap;
    }

    /*
    Reads a line part and returns the correct sensor readings.
     */
    private static int[] getSensorReadings(String linePart) {
        int[] sensorReadings;
        if (linePart.contains("@")) {
            String[] weightSplit = linePart.split("@");
            sensorReadings =
                    Arrays.stream(weightSplit[0].split(","))
                            .mapToInt(Integer::parseInt).toArray();
        } else {
            sensorReadings =
                    Arrays.stream(linePart.split(","))
                            .mapToInt(Integer::parseInt).toArray();
        }
        return sensorReadings;
    }

    /*
    Reads a line part and returns the correct weighting
     */
    private static int getWeighting(String linePart) {
        int weighting;
        if (linePart.contains("@")) {
            String[] weightSplit = linePart.split("@");
            weighting = Integer.parseInt(weightSplit[1]);
        } else {
            weighting = -1;
        }
        return weighting;
    }

    /*
    Reads a line part and returns the correct integer value
     */
    private static int getIntegerSplit(String linePart) {
        int value;
        if (linePart.contains("@")) {
            String[] weightSplit = linePart.split("@");
            value = Integer.parseInt(weightSplit[0]);
        } else {
            value = Integer.parseInt(linePart);
        }
        return value;
    }

    /*
    Creates a carbon dioxide sensor based on line parts
     */
    private static CarbonDioxideSensor createCO2Sensor(String[] lineParts, int[]
            sensorReadings) {
        int updateFrequency = Integer.parseInt(lineParts[2]);
        int idealValue = Integer.parseInt(lineParts[3]);
        int variationLimit = getIntegerSplit(lineParts[4]);
        return new CarbonDioxideSensor(sensorReadings, updateFrequency,
                idealValue, variationLimit);
    }

    /*
    Creates a noise sensor based on line parts
     */
    private static NoiseSensor createNoiseSensor(String[] lineParts, int[]
            sensorReadings) {
        int updateFrequency = getIntegerSplit(lineParts[2]);
        return new NoiseSensor(sensorReadings, updateFrequency);
    }

    /*
    Creates an occupancy sensor based on line parts.
     */
    private static OccupancySensor createOccupancySensor(String[] lineParts,
            int[] sensorReadings) {
        int updateFrequency = Integer.parseInt(lineParts[2]);
        int capacity = getIntegerSplit(lineParts[3]);
        return new OccupancySensor(sensorReadings, updateFrequency, capacity);
    }
}