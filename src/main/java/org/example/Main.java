package org.example;

import dto.Resident;
import dto.ApartmentBlock;
import dto.DTO;

import java.io.*;
import java.util.*;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {

        ApartmentBlock block1 = ApartmentBlock.builder()
                .blockId(1)
                .blockName("Rose")
                .build();

        Resident res1 = Resident.builder()
                .pesel("89101122334")
                .name("Alice")
                .surname("Smith")
                .age(30)
                .flatNumber(19)
                .apartment(block1)
                .build();

        Resident res2 = Resident.builder()
                .pesel("76111234567")
                .name("Michael")
                .surname("Johnson")
                .age(45)
                .flatNumber(21)
                .apartment(block1)
                .build();

        Resident res3 = Resident.builder()
                .pesel("88050567890")
                .name("Emily")
                .surname("Davis")
                .age(29)
                .flatNumber(11)
                .apartment(block1)
                .build();

        block1.addResident(res1);
        block1.addResident(res2);
        block1.addResident(res3);

        ApartmentBlock block2 = ApartmentBlock.builder()
                .blockId(2)
                .blockName("Daffodil")
                .build();

        Resident res4 = Resident.builder()
                .pesel("74658374658")
                .name("Carol")
                .surname("Sieniewicz")
                .age(23)
                .flatNumber(43)
                .apartment(block2)
                .build();

        Resident res5 = Resident.builder()
                .pesel("84759375643")
                .name("John")
                .surname("Johnson")
                .age(65)
                .flatNumber(23)
                .apartment(block2)
                .build();

        Resident res6 = Resident.builder()
                .pesel("24153967564")
                .name("Patricia")
                .surname("Singleton")
                .age(19)
                .flatNumber(25)
                .apartment(block2)
                .build();

        block2.addResident(res4);
        block2.addResident(res5);
        block2.addResident(res6);

        //System.out.println("Apartment block details:\n" + block1);

        //task 2
        System.out.println("\nTask 2:");

        block1.getResidents().forEach(resident -> {
            System.out.println("  Resident Details:");
            System.out.println("  Name: " + resident.getName());
            System.out.println("  Surname: " + resident.getSurname());
            System.out.println("  Age: " + resident.getAge());
            System.out.println("  Pesel: " + resident.getPesel());
        });

        //task 3
        System.out.println("\nTask 3:");

        Set<Resident> allResidents = Stream.of(block1.getResidents(), block2.getResidents())
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        allResidents.stream()
                .forEach(System.out::println);

        //task 4
        System.out.println("\nTask 4:");

        allResidents.stream()
                .filter(resident -> resident.getAge() >= 30)
                .sorted(Comparator.comparing(Resident::getName))
                .forEach(System.out::println);

        //task 5
        System.out.println("\nTask 5:");

        List<DTO> allResidentsDTO = allResidents.stream()
                .map(DTO::new)
                .sorted()
                .toList();

        allResidentsDTO.stream()
                .forEach(System.out::println);

        //task 6
        System.out.println("\nTask 6:");

        Set<ApartmentBlock> apartmentBlocks = new HashSet<>();
        apartmentBlocks.add(block1);
        apartmentBlocks.add(block2);

        serialize(apartmentBlocks, "apartmentBlocks.bin");
        Set<ApartmentBlock> deserializedBlocks = deserialize("apartmentBlocks.bin");

        deserializedBlocks.stream()
                .forEach(block -> {
                    System.out.println("Block info:\n" + block);
                    System.out.println("Residents:");
                    block.getResidents().forEach(resident ->
                            System.out.println(resident));
                    System.out.println();
                });

        //task 7
        System.out.println("\nTask 7:");

        List<ApartmentBlock> blocks = new ArrayList<>(deserializedBlocks);
        ForkJoinPool threadCustom = new ForkJoinPool(2);

        try {
            List<ForkJoinTask<Void>> tasks = new ArrayList<>();

            for (ApartmentBlock block : blocks) {
                ForkJoinTask<Void> task = threadCustom.submit(() -> {
                    try {
                        System.out.println("Residents are starting to enter and leave " + block.getBlockName() + " block.");
                        for (Resident resident : block.getResidents()) {
                            System.out.println("Resident just entered the block: " + resident);
                            Thread.sleep(500);
                            System.out.println("Resident just left the block: " + resident);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    return null;
                });

                tasks.add(task);
            }

            for (ForkJoinTask<Void> task : tasks) {
                task.join();
            }
        } catch (Exception err) {
            err.printStackTrace();
        } finally {
            threadCustom.shutdown();
        }


        //task7(deserializedBlocks);



    }

    private static void serialize(Object object, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static <T> T deserialize(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void task7(Set<ApartmentBlock> blocks) {
        ForkJoinPool t = new ForkJoinPool(2);
        try {
            blocks.parallelStream()
                    .forEach(block -> {
                        t.execute(() -> {
                            System.out.println("Residents are starting to enter and leave " + block.getBlockName() + " block.");
                            List<Resident> residents = block.getResidents();
                                try {
                                    for (Resident resident : residents) {
                                        System.out.println("Resident just entered the block: " + resident);
                                        Thread.sleep(500);
                                        System.out.println("Resident just left the block: " + resident);

                                    }
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                }
                        });
                    });
        } catch (Exception err) {
            err.printStackTrace();
        } finally {
            t.shutdown();
        }
        /*
        List<ApartmentBlock> blocks = new ArrayList<>(deserializedBlocks);

        ForkJoinPool threadCustom = new ForkJoinPool(2);
        try {
            blocks.parallelStream()
                    .forEach(block -> {
                        threadCustom.execute(() -> {
                            try {
                                System.out.println("Residents are starting to enter and leave " + block.getBlockName() + " block.");
                                for (Resident resident : block.getResidents()) {
                                    System.out.println("Resident just entered the block: " + resident);
                                    Thread.sleep(500);
                                    System.out.println("Resident just left the block: " + resident);

                                }
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        });
                    });
        } catch (Exception err) {
            err.printStackTrace();
        } finally {
            threadCustom.shutdown();
        }*/
    }
}