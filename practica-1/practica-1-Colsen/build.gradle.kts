plugins {
    id("java")
    id("application")
}

group = "trabajo-clase"
version = "1.0-SNAPSHOT"

application{
    mainClass= "practica-1.src.main"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")


    //dependencia de Javalin
    implementation("io.javalin:javalin:6.1.3")

    //manejo de log.
    implementation("org.slf4j:slf4j-simple:2.0.10")

    //libreria jsoup.
    implementation(group = "org.jsoup", name = "jsoup", version = "1.15.3")


    //Procesamiento JSON.
    implementation("com.fasterxml.jackson.core:jackson-core:2.15.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.15.2")

    // En la versión 5.3.X separaron las clases del renderizado en otro proyecto
    implementation ("io.javalin:javalin-rendering:6.1.3")

    //sistemas de plantilla:
    implementation ("org.freemarker:freemarker:2.3.32")
    implementation ("org.thymeleaf:thymeleaf:3.1.1.RELEASE")
    implementation ("org.apache.velocity:velocity-engine-core:2.3")
}

tasks.test {
    useJUnitPlatform()
}