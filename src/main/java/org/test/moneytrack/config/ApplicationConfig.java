package org.test.moneytrack.config;

import org.test.moneytrack.application.port.OutputRepository;
import org.test.moneytrack.infrastructure.output.FileOutputRepository;
import org.test.moneytrack.infrastructure.repository.FileUserRepository;
import org.test.moneytrack.infrastructure.repository.InMemoryUserRepository;
import org.test.moneytrack.infrastructure.storage.JsonFileStorage;
import org.test.moneytrack.domain.service.*;
import org.test.moneytrack.application.usecase.*;

public class ApplicationConfig {

    // Репозитории
    private final InMemoryUserRepository inMemoryRepository;
    private final FileUserRepository fileRepository;
    private final OutputRepository outputRepository;

    // Доменные сервисы
    private final WalletDomainService walletDomainService;
    private final BudgetDomainService budgetDomainService;
    private final TransferDomainService transferDomainService;

    // Use Cases
    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;
    private final AddIncomeUseCase addIncomeUseCase;
    private final AddExpenseUseCase addExpenseUseCase;
    private final SetBudgetUseCase setBudgetUseCase;
    private final GetSummaryUseCase getSummaryUseCase;
    private final GetBudgetStatusUseCase getBudgetStatusUseCase;
    private final ShowExpensesUseCase getShowExpensesUseCase;
    private final TransferFundsUseCase transferFundsUseCase;
    private final LoadUserDataUseCase loadUserDataUseCase;
    private final SaveUserDataUseCase saveUserDataUseCase;

    public ApplicationConfig() {
        JsonFileStorage storage = new JsonFileStorage("data");
        this.fileRepository = new FileUserRepository(storage);
        this.inMemoryRepository = new InMemoryUserRepository();
        this.outputRepository = new FileOutputRepository();

        // Доменные сервисы
        this.walletDomainService = new WalletDomainService();
        this.budgetDomainService = new BudgetDomainService();
        this.transferDomainService = new TransferDomainService(walletDomainService);

        // Use Cases (работают с inMemory)
        this.registerUserUseCase = new RegisterUserUseCase(inMemoryRepository);
        this.loginUserUseCase = new LoginUserUseCase(inMemoryRepository);
        this.addIncomeUseCase = new AddIncomeUseCase(inMemoryRepository, walletDomainService);
        this.addExpenseUseCase = new AddExpenseUseCase(inMemoryRepository, walletDomainService);
        this.setBudgetUseCase = new SetBudgetUseCase(inMemoryRepository, budgetDomainService);
        this.getSummaryUseCase = new GetSummaryUseCase(walletDomainService, outputRepository);
        this.getBudgetStatusUseCase = new GetBudgetStatusUseCase(walletDomainService, outputRepository);
        this.getShowExpensesUseCase = new ShowExpensesUseCase(walletDomainService, outputRepository);
        this.transferFundsUseCase = new TransferFundsUseCase(inMemoryRepository, transferDomainService);

        // для загрузки и сохранения данных
        this.loadUserDataUseCase = new LoadUserDataUseCase(fileRepository, inMemoryRepository);
        this.saveUserDataUseCase = new SaveUserDataUseCase(fileRepository);
    }

    // Геттеры для репозиториев
    public InMemoryUserRepository getInMemoryRepository() {
        return inMemoryRepository;
    }

    public FileUserRepository getFileRepository() {
        return fileRepository;
    }

    // Геттеры для Use Cases
    public RegisterUserUseCase getRegisterUserUseCase() {
        return registerUserUseCase;
    }

    public LoginUserUseCase getLoginUserUseCase() {
        return loginUserUseCase;
    }

    public AddIncomeUseCase getAddIncomeUseCase() {
        return addIncomeUseCase;
    }

    public AddExpenseUseCase getAddExpenseUseCase() {
        return addExpenseUseCase;
    }

    public SetBudgetUseCase getSetBudgetUseCase() {
        return setBudgetUseCase;
    }

    public GetSummaryUseCase getGetSummaryUseCase() {
        return getSummaryUseCase;
    }

    public GetBudgetStatusUseCase getGetBudgetStatusUseCase() {
        return getBudgetStatusUseCase;
    }

    public ShowExpensesUseCase getGetShowExpensesUseCase() {
        return getShowExpensesUseCase;
    }

    public TransferFundsUseCase getTransferFundsUseCase() {
        return transferFundsUseCase;
    }

    public LoadUserDataUseCase getLoadUserDataUseCase() {
        return loadUserDataUseCase;
    }

    public SaveUserDataUseCase getSaveUserDataUseCase() {
        return saveUserDataUseCase;
    }

    public OutputRepository getOutputRepository() {
        return outputRepository;
    }

}
