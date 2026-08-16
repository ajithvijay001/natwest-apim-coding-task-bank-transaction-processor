# natwest-apim-coding-task-bank-transaction-processor
Bank Transaction Processor - coding assessment for Maveric Systems / NatWest Group.

Design Decisions & Trade-offs
- Account ID generation: IDs are randomly generated withint a 16 digit range rather than checked for uniquness against existing accounts. Given the timeline, this is a trade-off. A production system would enforce uniquness explicitly.
- Zero initial balance alllowed: Accounts can be created with a balance of 0. this reflects real-world banking, where an account is often opened before being funded via a separate deposit.