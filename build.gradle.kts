
plugins {
    id("java")
    id("application")
}

group = "trabajo-clase"
version = "1.0-SNAPSHOT"

application{
        mainClass= "Main"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    //Procesamiento JSON para Javalin.
    implementation("com.fasterxml.jackson.core:jackson-core:2.15.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.15.2")

    //dependencia de Javalin
    implementation("io.javalin:javalin:6.1.3")
//manejo de log.
    implementation("org.slf4j:slf4j-simple:2.0.11")
// En la versión 5.3.X separaron las clases del renderizado en otro proyecto
    implementation("io.javalin:javalin-rendering:6.1.3")

    //ORM - Hibernate.
    implementation("org.hibernate.orm:hibernate-core:6.0.2.Final")
//implementation 'org.hibernate:hibernate-entitymanager:6.0.0.Alpha7'
//Driver de la conexión en h2...
    implementation("com.h2database:h2:2.1.214")
    implementation("org.postgresql:postgresql:42.3.8")
//sistemas de plantilla:
    implementation("org.thymeleaf:thymeleaf:3.1.1.RELEASE")



}

tasks.test {
    useJUnitPlatform()
}