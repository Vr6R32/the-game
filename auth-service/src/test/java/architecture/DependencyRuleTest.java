package architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class DependencyRuleTest {

        @Test
        void servicesShouldNotDependOnAuthService() {

            JavaClasses importedClasses = new ClassFileImporter().importPackages("com.thegame");

            ArchRule rule = noClasses()
                    .that().resideInAPackage("..jwt..")
                    .should().dependOnClassesThat().resideInAPackage("..authentication..");

            rule.check(importedClasses);

    }
}
