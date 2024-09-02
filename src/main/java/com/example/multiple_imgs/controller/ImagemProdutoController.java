package com.example.multiple_imgs.controller;

import com.example.multiple_imgs.model.ImagemProduto;
import com.example.multiple_imgs.model.Produto;
import com.example.multiple_imgs.repository.ImagemProdutoRepository;
import com.example.multiple_imgs.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/imagem-produto")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ImagemProdutoController {

    @Autowired
    private ImagemProdutoRepository imagemProdutoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @PostMapping("/upload/{refs}")
    public ResponseEntity<?> addPhotos(@RequestParam("images") List<MultipartFile> files, @PathVariable String refs) {
        try {
            List<ImagemProduto> imagens = new ArrayList<>();

            for (MultipartFile file : files) {
                ImagemProduto imagemProduto = new ImagemProduto();
                InputStream is = file.getInputStream();
                byte[] bytes = new byte[(int) file.getSize()];
                IOUtils.readFully(is, bytes);
                imagemProduto.setImagem(bytes);
                imagemProduto.setRefsImg(refs);
                imagens.add(imagemProduto);
            }

            List<ImagemProduto> savedImages = imagemProdutoRepository.saveAll(imagens);

            return ResponseEntity.ok(savedImages);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar as imagens: " + e.getMessage());
        }
    }

    @PostMapping("/upload-img/{refs}")
    public ResponseEntity<?> addPhotos(@RequestParam("image") MultipartFile file, @PathVariable String refs) {
        try {
            ImagemProduto imagemProduto = new ImagemProduto();
            InputStream is = file.getInputStream();
            byte[] bytes = new byte[(int) file.getSize()];
            IOUtils.readFully(is, bytes);
            imagemProduto.setImagem(bytes);
            imagemProduto.setRefsImg(refs);

            ImagemProduto savedImage = imagemProdutoRepository.save(imagemProduto);

            return ResponseEntity.ok(savedImage);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar a imagem: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ImagemProduto>> findAll() {
        return ResponseEntity.ok(imagemProdutoRepository.findAll());
    }
}
