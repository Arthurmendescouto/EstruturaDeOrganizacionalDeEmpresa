package org.example;

import java.util.ArrayList;
import java.util.List;

public class Pessoa {
    // Atributos definidos no Passo 1
    private String nome;
    private String cargo;
    private String departamento;
    private Double salario;

    // Atributos necessários para a hierarquia (Passo 2 em diante)
    private List<Pessoa> subordinados;
    private Pessoa superior;

    public Pessoa(String nome, String cargo, String departamento, Double salario) {
        this.nome = nome;
        this.cargo = cargo;
        this.departamento = departamento;
        this.salario = salario;
        this.subordinados = new ArrayList<>();
        this.superior = null;
    }

    public String getNome() { return nome; }
    public String getCargo() { return cargo; }
    public String getDepartamento() { return departamento; }
    public Double getSalario() { return salario; }

    public List<Pessoa> getSubordinados() { return subordinados; }
    public Pessoa getSuperior() { return superior; }
    public void setSuperior(Pessoa superior) { this.superior = superior; }

    @Override
    public String toString() {
        return "Nome: " + nome + ", Cargo: " + cargo + ", Depto: " + departamento +
                (salario != null ? ", Salário: R$" + String.format("%.2f", salario) : "");
    }
}