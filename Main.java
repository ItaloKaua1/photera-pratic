import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final NumberFormat FORMATO_NUMERICO;
    static {
        FORMATO_NUMERICO = NumberFormat.getInstance(Locale.of("pt", "BR"));
        FORMATO_NUMERICO.setMinimumFractionDigits(2);
        FORMATO_NUMERICO.setMaximumFractionDigits(2);
    }

    private static final BigDecimal SALARIO_MINIMO = new BigDecimal("1212.00");

    public static void main(String[] args) {

        // Bloco de todos os funcionários = tabela
        List<Funcionario> funcionarios = new ArrayList<>();
        funcionarios.add(new Funcionario("Maria", LocalDate.of(2000, 10, 18), new BigDecimal("2009.44"), "Operador"));
        funcionarios.add(new Funcionario("João", LocalDate.of(1990, 5, 12), new BigDecimal("2284.38"), "Operador"));
        funcionarios.add(new Funcionario("Caio", LocalDate.of(1961, 5, 2), new BigDecimal("9836.14"), "Coordenador"));
        funcionarios.add(new Funcionario("Miguel", LocalDate.of(1988, 10, 14), new BigDecimal("19119.88"), "Diretor"));
        funcionarios.add(new Funcionario("Alice", LocalDate.of(1995, 1, 5), new BigDecimal("2234.68"), "Recepcionista"));
        funcionarios.add(new Funcionario("Heitor", LocalDate.of(1999, 11, 19), new BigDecimal("1582.72"), "Operador"));
        funcionarios.add(new Funcionario("Arthur", LocalDate.of(1993, 3, 31), new BigDecimal("4071.84"), "Contador"));
        funcionarios.add(new Funcionario("Laura", LocalDate.of(1994, 7, 8), new BigDecimal("3017.45"), "Gerente"));
        funcionarios.add(new Funcionario("Heloísa", LocalDate.of(2003, 5, 24), new BigDecimal("1606.85"), "Eletricista"));
        funcionarios.add(new Funcionario("Helena", LocalDate.of(1996, 9, 2), new BigDecimal("2799.93"), "Gerente"));

        funcionarios.removeIf(f -> f.getNome().equals("João"));

        System.out.println("3.3 - Lista de funcionários ");
        funcionarios.forEach(Main::imprimirFuncionario);

        funcionarios.forEach(f -> f.setSalario(
                f.getSalario().multiply(new BigDecimal("1.10")).setScale(2, RoundingMode.HALF_UP)));

        System.out.println("\n3.4 - Salários após aumento de 10% ");
        funcionarios.forEach(Main::imprimirFuncionario);

        Map<String, List<Funcionario>> funcionariosPorFuncao = funcionarios.stream()
                .collect(Collectors.groupingBy(Funcionario::getFuncao));

        System.out.println("\n3.6 - Funcionários agrupados por função ");
        funcionariosPorFuncao.forEach((funcao, lista) -> {
            System.out.println("Função: " + funcao);
            lista.forEach(Main::imprimirFuncionario);
        });

        System.out.println("\n3.8 - Aniversariantes de outubro e dezembro ");
        funcionarios.stream()
                .filter(f -> f.getDataNascimento().getMonthValue() == 10
                        || f.getDataNascimento().getMonthValue() == 12)
                .forEach(Main::imprimirFuncionario);

        Funcionario maisVelho = funcionarios.stream()
                .max(Comparator.comparing(f -> calcularIdade(f.getDataNascimento())))
                .orElse(null);

        System.out.println("\n3.9 - Funcionário com maior idade ");
        if (maisVelho != null) {
            System.out.println("Nome: " + maisVelho.getNome()
                    + " | Idade: " + calcularIdade(maisVelho.getDataNascimento()) + " anos");
        }

        List<Funcionario> ordemAlfabetica = new ArrayList<>(funcionarios);
        ordemAlfabetica.sort(Comparator.comparing(Funcionario::getNome));

        System.out.println("\n3.10 - Funcionários em ordem alfabética ");
        ordemAlfabetica.forEach(Main::imprimirFuncionario);

        BigDecimal totalSalarios = funcionarios.stream()
                .map(Funcionario::getSalario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println("\n3.11 - Total dos salários ");
        System.out.println("Total: " + FORMATO_NUMERICO.format(totalSalarios));

        System.out.println("\n3.12 - Salários mínimos por funcionário (mínimo = "
                + FORMATO_NUMERICO.format(SALARIO_MINIMO) + ") ");
        funcionarios.forEach(f -> {
            BigDecimal quantidadeSalarios = f.getSalario().divide(SALARIO_MINIMO, 2, RoundingMode.HALF_UP);
            System.out.println(f.getNome() + " -> " + FORMATO_NUMERICO.format(quantidadeSalarios) + " salários mínimos");
        });
    }

    private static void imprimirFuncionario(Funcionario funcionario) {
        System.out.println(String.format(
                "Nome: %-10s | Nascimento: %s | Salário: %12s | Função: %s",
                funcionario.getNome(),
                funcionario.getDataNascimento().format(FORMATO_DATA),
                FORMATO_NUMERICO.format(funcionario.getSalario()),
                funcionario.getFuncao()));
    }

    private static int calcularIdade(LocalDate dataNascimento) {
        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }
}