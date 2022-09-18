package ru.cubly.firefly.importer.client

import ru.cubly.firefly.api.AccountsApi
import ru.cubly.firefly.api.TransactionsApi
import ru.cubly.firefly.model.AccountRead
import ru.cubly.firefly.model.AccountTypeFilter
import ru.cubly.firefly.model.TransactionSplitStore
import ru.cubly.firefly.model.TransactionStore

class FireflyClient(
    private val accountsApi: AccountsApi,
    private val transactionsApi: TransactionsApi
) {

    suspend fun listAssetAccounts(): List<AccountRead> {
        val accounts = mutableListOf<AccountRead>()
        var pageNum = 1

        do {
            val page = accountsApi.listAccount(pageNum, type = AccountTypeFilter.AssetAccount, date = null)
            val body = page.body()

            accounts.addAll(body.data)
            pageNum++
        } while (pageNum < (body.meta.pagination?.totalPages ?: -1))

        return accounts
    }

    suspend fun addTransaction(transaction: TransactionSplitStore) {
        transactionsApi.storeTransaction(
            TransactionStore(
                listOf(transaction),
                errorIfDuplicateHash = true,
                applyRules = true,
                fireWebhooks = true
            )
        )
    }
}