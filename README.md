# natwest-apim-coding-task-bank-transaction-processor
Bank Transaction Processor - coding assessment for Maveric Systems / NatWest Group.

## Design Decisions & Trade-offs
- Account ID generation: IDs are randomly generated within a 16 digit range rather than checked for uniqueness against existing accounts. Given the timeline, this is a trade-off. A production system would enforce uniqueness explicitly.
- Zero initial balance allowed: Accounts can be created with a balance of 0. this reflects real-world banking, where an account is often opened before being funded via a separate deposit.
- Storage: A Prod system would use persistence storage(eg: a database), considering the timeline, an in-memory Map is used.
- Accounts are mutable and updated in place rather than immutable, for simplicity. An immutable design would be preferable in concurrent prod environment.
- Transaction records are immutable once created, unlike Accounts, since a ledger entry represents a historical fact that should never change once recorded.
- Transaction is implemented as a Java record rather than a class, since it is an immutable data carrier.
- Transaction history query returns full history without date filtering; date-range filtering was considered but descoped given the timebox to prioritize core operations.
- Empty transaction history: Querying transaction history for an account with no transactions returns an empty list rather than throwing an exception, since it is a valid state, not an error.