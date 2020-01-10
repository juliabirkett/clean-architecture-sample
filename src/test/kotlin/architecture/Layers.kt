package architecture


import com.tngtech.archunit.base.DescribedPredicate.not
import com.tngtech.archunit.core.domain.JavaClass.Predicates.belongToAnyOf
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.lang.conditions.ArchPredicates.are
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import com.tngtech.archunit.library.Architectures.layeredArchitecture
import org.junit.jupiter.api.Test
import users.WebAppConfig

object Layers {
    private val classes = ClassFileImporter()
        .withImportOption(DoNotIncludeTests())
        .importPackages("users")

    @Test
    fun `no one calls handlers directly`() {
        layeredArchitecture()
            .layer("web handlers").definedBy("..web.handlers")
            .layer("use cases").definedBy("..usecases")
            .layer("persistence").definedBy("..persistence")
            .whereLayer("web handlers").mayNotBeAccessedByAnyLayer()
            .whereLayer("use cases").mayOnlyBeAccessedByLayers("web handlers")
            .whereLayer("persistence").mayOnlyBeAccessedByLayers("use cases")
            .check(classes.that(are(not(belongToAnyOf(WebAppConfig::class.java)))))
    }

    @Test
    fun test() {
        noClasses()
            .that().resideOutsideOfPackage("..web.handlers")
            .should().accessClassesThat().resideInAnyPackage("io.javalin..")
            .because("Javalin is to be used by the web adapter")
            .check(classes.that(are(not(belongToAnyOf(WebAppConfig::class.java)))))
    }
}