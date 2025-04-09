package org.sophia.nodecode.logicSystems.core;

/** Important to note: Requests are just a way to cleanly handle the data. This does not do any of the pushing/pulling, other systems handle that
 * @param node - The {@link Node} this request will point to
 * @param pullSlot The slot this node will try to pull from
 * @param targetSlot The target this request will try to push from
 */
public record Request(Node node, Integer pullSlot, Integer targetSlot) {
}
