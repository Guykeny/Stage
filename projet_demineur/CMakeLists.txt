cmake_minimum_required(VERSION 3.18)
project(Projet LANGUAGES CXX)

set(CMAKE_INCLUDE_CURRENT_DIR ON)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

set(CMAKE_AUTOMOC ON)
find_package(Qt6 COMPONENTS Widgets  LinguistTools REQUIRED)
set(TS_FILES
    fr_FR.ts
    en_US.ts
)

qt_create_translation(QM_FILES ${CMAKE_SOURCE_DIR} ${TS_FILES})

qt_add_resources(RESOURCE_FILES resource.qrc)

add_executable(Projet main.cpp projet.cpp projet.h ${RESOURCE_FILES} ${QM_FILES})
target_link_libraries(Projet PRIVATE Qt6::Widgets)
