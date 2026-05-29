package raposinha.houseHoldChores.runner;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import raposinha.houseHoldChores.entities.Category;
import raposinha.houseHoldChores.entities.PresetTask;
import raposinha.houseHoldChores.entities.Task;
import raposinha.houseHoldChores.exception.NotFoundException;
import raposinha.houseHoldChores.repositories.CategoryRepo;
import raposinha.houseHoldChores.repositories.PresetTaskRepo;
import raposinha.houseHoldChores.repositories.TaskRepo;
import raposinha.houseHoldChores.service.TaskService;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@Order(2)
@AllArgsConstructor
public class TaskDataRunner implements CommandLineRunner {

    private final TaskRepo taskRepo;
    private final CategoryRepo categoryRepo;
    public final PresetTaskRepo presetTaskRepo;


    @Override
    public void run(String... args) throws Exception {
        //Check 'presetTaskRepo' instead of 'taskRepo' so it matches what we insert!
        if (presetTaskRepo.count() == 0) { // Only seed if the table is empty
            System.out.println("SEEDER: Starting Task seeding...");
            InputStream taskStream = new ClassPathResource("csv/tasks.csv").getInputStream();
            importPresetTasks(taskStream);

            System.out.println("SEEDER: Task seeding completed.");
        }
    }

    // import preset tasks
    private void importPresetTasks(InputStream inputStream) throws Exception {
        CSVParser parser = new CSVParserBuilder().withSeparator(',').withIgnoreQuotations(true).build();
        try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(inputStream))
                .withCSVParser(parser)
                .withSkipLines(1)
                .build()) {
            String[] fields;

            while ((fields = reader.readNext()) != null) {

                if (fields.length >= 3) {
                    String categoryName = fields[0].trim();
                    String title = fields[1].trim();
                    int frequency = Integer.parseInt(fields[2].trim());

                    Category category = categoryRepo.findByName(categoryName)
                            .orElseThrow(() -> new NotFoundException("Category '" + categoryName + "' was not found."));
                    PresetTask t = new PresetTask();
                    t.setCategory(category);
                    t.setTitle(title);
                    t.setFrequency(frequency);

                    presetTaskRepo.saveAndFlush(t);
                }
            }
        }
    }
}