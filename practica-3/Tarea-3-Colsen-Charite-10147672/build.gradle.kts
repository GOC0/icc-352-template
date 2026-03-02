import sun.jvmstat.monitor.MonitoredVmUtil.mainClass

plugins {
    id("java")
    id("application")
}

group = "comprender"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application{
    mainClass.set("comprend.Main");
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("io.javalin:javalin:6.7.0")
    implementation("io.javalin:javalin-rendering-thymeleaf:7.0.0")
    implementation("org.slf4j:slf4j-simple:2.0.10")
    implementation("org.jsoup:jsoup:1.22.1")

    implementation("com.fasterxml.jackson.core:jackson-core:2.15.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.15.2")

    implementation("org.thymeleaf:thymeleaf:3.1.2.RELEASE")

    //ORM - Hibernate.
    implementation("org.hibernate.orm:hibernate-core:6.0.2.Final")
    implementation("jakarta.persistence:jakarta.persistence-api:3.0.0")
    implementation("com.h2database:h2:2.2.220")
    implementation("org.postgresql:postgresql:42.7.7")

}

tasks.test {
    useJUnitPlatform()
}