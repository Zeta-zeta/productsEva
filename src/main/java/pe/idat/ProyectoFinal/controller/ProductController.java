package pe.idat.ProyectoFinal.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.idat.ProyectoFinal.model.Product;
import pe.idat.ProyectoFinal.repository.ProductRepository;

import java.util.List;

@Service
@Path("/products")
public class ProductController {
    @Autowired

    private ProductRepository productRepository;
    private static final ObjectMapper objectMapper = new ObjectMapper();


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProducto() {
        try {
            List<Product> persons = productRepository.findAll();
            String json = objectMapper.writeValueAsString(persons);
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        } catch (JsonProcessingException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al convertir a JSON")
                    .build();
        }
    }

    @GET
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductoId(@PathParam("name") String name) {
        Product product = productRepository.findByName(name);
        if (product != null) {
            try {
                String json = objectMapper.writeValueAsString(product);
                return Response.ok(json, MediaType.APPLICATION_JSON).build();
            } catch (JsonProcessingException e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Error al convertir a JSON")
                        .build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"message\":\"Producto no encontrado\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @PUT
    @Path("/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProducto(@PathParam("name") String name, String json) {
        try {
            Product updateProduct = objectMapper.readValue(json, Product.class);
            Product product = productRepository.findByName(name);
            if (product != null) {
                product.setStock(updateProduct.getStock());
                product.setPrice(updateProduct.getPrice());
                productRepository.save(product);
                String responseMessage = "{\"message\":\"Producto actualizado correctamente\"}";
                return Response.status(Response.Status.OK)
                        .entity(responseMessage)
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
            return Response.status(Response.Status.NOT_FOUND).entity("Producto no encontrado").build();
        } catch (JsonProcessingException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al procesar la solicitud")
                    .build();
        }
    }

    @DELETE
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteProducto(@PathParam("name") String name) {
        Product product = productRepository.findByName(name);
        if (product != null) {
            productRepository.delete(product);
            String responseMessage = "{\"message\":\"Producto eliminado correctamente\"}";
            return Response.status(Response.Status.NO_CONTENT)
                    .entity(responseMessage)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"message\":\"Producto no encontrado\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProducto(String json) {
        try {
            Product newPerson = objectMapper.readValue(json, Product.class);
            productRepository.save(newPerson);
            String createdJson = objectMapper.writeValueAsString(newPerson);
            return Response.status(Response.Status.CREATED)
                    .entity("Producto creado correctamente")
                    .build();
        } catch (JsonProcessingException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al procesar la solicitud")
                    .build();
        }
    }
}
