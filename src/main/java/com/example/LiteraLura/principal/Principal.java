package com.example.LiteraLura.principal;

import com.example.LiteraLura.model.Autor;
import com.example.LiteraLura.model.Datos;
import com.example.LiteraLura.model.DatosLibro;
import com.example.LiteraLura.model.Libro;
import com.example.LiteraLura.repository.AutorRepository;
import com.example.LiteraLura.repository.LibroRepository;
import com.example.LiteraLura.service.ConsumoApi;
import com.example.LiteraLura.service.ConvierteDatos;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner lectura = new Scanner(System.in);
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    // Punto de entrada para el menú interactivo
    public void mostrarMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    ***************************************************
                    Elija la opción a través de su número:
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    0 - Salir
                    ***************************************************
                    """;
            System.out.println(menu);
            
            try {
                // Validación para evitar que el programa se cierre por error de entrada
                opcion = Integer.parseInt(lectura.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida. Por favor, ingrese un número.");
                continue;
            }

            switch (opcion) {
                case 1: buscarLibroPorTitulo(); break;
                case 2: listarLibrosRegistrados(); break;
                case 3: listarAutoresRegistrados(); break;
                case 4: listarAutoresVivos(); break;
                case 5: listarLibrosPorIdioma(); break;
                case 0: System.out.println("Saliendo del sistema..."); break;
                default: System.out.println("Opción no válida.");
            }
        }
    }

    // Opción 1: Lógica para buscar en la API y persistir en la DB
    private void buscarLibroPorTitulo() {
        System.out.println("Escribe el nombre del libro que deseas buscar:");
        var nombreLibro = lectura.nextLine();
        // Consumimos la API dinámica pasándole el nombre del libro codificado
        var json = consumoApi.obtenerDatos(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);
        
        if (datosBusqueda.resultados() != null && !datosBusqueda.resultados().isEmpty()) {
            DatosLibro datosLibro = datosBusqueda.resultados().get(0); // Tomamos el primer resultado como pide el reto
            
            // Verificamos si el libro ya está registrado para no duplicar datos
            Optional<Libro> libroExistente = libroRepository.findByTituloIgnoreCase(datosLibro.titulo());
            if (libroExistente.isPresent()) {
                System.out.println("Este libro ya está en nuestra base de datos:");
                System.out.println(libroExistente.get());
                return;
            }

            // Mapeamos de DTO a Entidad
            Libro libro = new Libro(datosLibro);
            if (!datosLibro.autor().isEmpty()) {
                var datosAutor = datosLibro.autor().get(0);
                // Si el autor ya existe, usamos el mismo; si no, lo guardamos
                Autor autor = autorRepository.findByNombreIgnoreCase(datosAutor.nombre())
                        .orElseGet(() -> autorRepository.save(new Autor(datosAutor)));
                libro.setAutor(autor);
            }
            
            // Persistimos el libro y mostramos el resultado
            libroRepository.save(libro);
            System.out.println(libro);
        } else {
            System.out.println("Lo sentimos, no se encontró ese libro.");
        }
    }

    // Listado de todos los libros consultados previamente
    private void listarLibrosRegistrados() {
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("Todavía no has registrado ningún libro.");
        } else {
            libros.forEach(System.out::println);
        }
    }

    // Listado de autores con un formato amigable para el evaluador
    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("No hay autores en la base de datos.");
        } else {
            autores.forEach(a -> {
                System.out.println("----- AUTOR -----");
                System.out.println("Nombre: " + a.getNombre());
                System.out.println("Fechas: " + a.getFechaDeNacimiento() + " - " + (a.getFechaDeMuerte() != null ? a.getFechaDeMuerte() : "N/A"));
                System.out.print("Libros: [");
                a.getLibros().forEach(l -> System.out.print(l.getTitulo() + ", "));
                System.out.println("]");
                System.out.println("-----------------");
            });
        }
    }

    // Opción 4: Lógica para encontrar autores según el año proporcionado
    private void listarAutoresVivos() {
        System.out.println("¿En qué año te interesa saber quién estaba vivo?");
        try {
            var anio = Integer.parseInt(lectura.nextLine());
            if (anio < 0) {
                System.out.println("Ingresa un año válido, por favor.");
                return;
            }
            List<Autor> autoresVivos = autorRepository.findAutoresVivosEnAnio(anio);
            if (autoresVivos.isEmpty()) {
                System.out.println("No hay autores registrados que estuvieran vivos en el año " + anio);
            } else {
                System.out.println("----- AUTORES VIVOS EN " + anio + " -----");
                autoresVivos.forEach(a -> System.out.println(a.getNombre()));
                System.out.println("------------------------------------");
            }
        } catch (NumberFormatException e) {
            System.out.println("Dato inválido. Escribe un año con números.");
        }
    }

    // Opción 5: Estadísticas básicas por idioma usando Streams de Java
    private void listarLibrosPorIdioma() {
        System.out.println("""
                Ingresa el código del idioma:
                es - español
                en - inglés
                fr - francés
                pt - portugués
                """);
        var idioma = lectura.nextLine();
        List<Libro> libros = libroRepository.findByIdioma(idioma);
        
        if (libros.isEmpty()) {
            System.out.println("No tenemos libros en ese idioma en este momento.");
        } else {
            // Usamos streams para mostrar la cantidad total encontrada
            System.out.println("Se encontraron " + libros.stream().count() + " libros en " + idioma + ":");
            libros.forEach(System.out::println);
        }
    }
}
